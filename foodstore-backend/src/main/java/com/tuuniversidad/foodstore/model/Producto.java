package com.tuuniversidad.foodstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "producto")
public class Producto extends Base {

    @Column(nullable = false, length = 200)
    private String nombre;

    /** RN-011-06: precio monetario con precisión decimal. */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false)
    private int stock;

    @Column(length = 500)
    private String imagen;

    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Producto() {
        super();
    }

    public Producto(Long id, String nombre, BigDecimal precio, String descripcion, int stock,
                    String imagen, Boolean disponible, Categoria categoria) {
        super(id, false, null);
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        if (categoria != null) {
            categoria.agregarProducto(this);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria nueva) {
        if (this.categoria == nueva) {
            return;
        }
        if (this.categoria != null) {
            this.categoria.desanexarProducto(this);
        }
        this.categoria = nueva;
        if (nueva != null) {
            nueva.anexarProducto(this);
        }
    }

    public boolean estaDisponibleParaVenta() {
        return Boolean.TRUE.equals(disponible) && stock > 0;
    }

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', precio=" + precio
                + ", stock=" + stock + ", disponible=" + disponible
                + ", categoria=" + (categoria != null ? categoria.getNombre() : null) + "}";
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
        Producto producto = (Producto) o;
        return stock == producto.stock
                && Objects.equals(nombre, producto.nombre)
                && Objects.equals(precio, producto.precio)
                && Objects.equals(descripcion, producto.descripcion)
                && Objects.equals(imagen, producto.imagen)
                && Objects.equals(disponible, producto.disponible);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nombre, precio, descripcion, stock, imagen, disponible);
    }
}
