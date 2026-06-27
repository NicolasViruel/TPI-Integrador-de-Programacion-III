package com.tuuniversidad.foodstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido extends Base {

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private Double subtotal;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public DetallePedido() {
        super();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void actualizarSubtotal() {
        if (producto != null && producto.getPrecio() != null) {
            // RN-011-06: precio × cantidad con BigDecimal
            this.subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(cantidad))
                    .doubleValue();
        } else {
            this.subtotal = 0d;
        }
    }

    @Override
    public String toString() {
        String nombreProducto = producto != null ? producto.getNombre() : "—";
        return cantidad + " x " + nombreProducto + " (subtotal: " + String.format("%.2f",
                subtotal != null ? subtotal : 0d) + ")";
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
        DetallePedido that = (DetallePedido) o;
        return cantidad == that.cantidad
                && Objects.equals(subtotal, that.subtotal)
                && Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cantidad, subtotal, producto);
    }
}
