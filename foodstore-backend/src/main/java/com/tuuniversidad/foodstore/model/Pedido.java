package com.tuuniversidad.foodstore.model;

import com.tuuniversidad.foodstore.model.enums.Estado;
import com.tuuniversidad.foodstore.model.enums.FormaPago;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "pedido")
public class Pedido extends Base implements Calculable {

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Estado estado;

    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false, length = 30)
    private FormaPago formaPago;

    @Column(length = 30)
    private String telefono;

    @Column(length = 255)
    private String direccion;

    @Column(name = "costo_envio")
    private Double costoEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetallePedido> detalles = new LinkedHashSet<>();

    public Pedido() {
        super();
        this.total = 0d;
    }

    public Pedido(Long id, LocalDate fecha, Usuario usuario, Estado estado, FormaPago formaPago) {
        super(id, false, null);
        this.fecha = fecha;
        this.usuario = usuario;
        this.estado = estado;
        this.formaPago = formaPago;
        this.total = 0d;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<DetallePedido> getDetalles() {
        return Collections.unmodifiableSet(detalles);
    }

    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        if (producto == null || cantidad <= 0) {
            return;
        }
        DetallePedido existente = findDetallePedidoByProducto(producto);
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            if (subtotal != null) {
                BigDecimal acumulado = BigDecimal.valueOf(
                        existente.getSubtotal() != null ? existente.getSubtotal() : 0d);
                existente.setSubtotal(acumulado.add(BigDecimal.valueOf(subtotal)).doubleValue());
            } else {
                existente.actualizarSubtotal();
            }
        } else {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(this);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            if (subtotal != null) {
                detalle.setSubtotal(subtotal);
            } else {
                detalle.actualizarSubtotal();
            }
            detalles.add(detalle);
        }
        calcularTotal();
    }

    public void addDetallePedido(int cantidad, Producto producto) {
        addDetallePedido(cantidad, null, producto);
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        return detalles.stream()
                .filter(d -> Objects.equals(d.getProducto(), producto))
                .findFirst()
                .orElse(null);
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalles.remove(detalle);
            detalle.setPedido(null);
            calcularTotal();
        }
    }

    public BigDecimal calcularSubtotal() {
        detalles.forEach(DetallePedido::actualizarSubtotal);
        return detalles.stream()
                .map(d -> d.getSubtotal() != null
                        ? BigDecimal.valueOf(d.getSubtotal())
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void calcularTotal() {
        BigDecimal subtotal = calcularSubtotal();
        BigDecimal envio = costoEnvio != null
                ? BigDecimal.valueOf(costoEnvio)
                : BigDecimal.ZERO;
        this.total = subtotal.add(envio).doubleValue();
    }

    public int calcularCantidadItems() {
        return detalles.stream()
                .mapToInt(DetallePedido::getCantidad)
                .sum();
    }

    public String resumenCantidadesPorProducto() {
        return detalles.stream()
                .map(d -> d.getCantidad() + " " + d.getProducto().getNombre())
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", fecha=" + fecha + ", estado=" + estado
                + ", formaPago=" + formaPago + ", total=" + String.format("%.2f",
                total != null ? total : 0d) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Pedido pedido = (Pedido) o;
        return Objects.equals(fecha, pedido.fecha)
                && Objects.equals(usuario, pedido.usuario)
                && estado == pedido.estado
                && formaPago == pedido.formaPago;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fecha, usuario, estado, formaPago);
    }
}
