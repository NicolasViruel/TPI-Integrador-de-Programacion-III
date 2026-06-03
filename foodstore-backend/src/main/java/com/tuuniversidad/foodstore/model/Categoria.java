package com.tuuniversidad.foodstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria")
public class Categoria extends Base {

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 500)
    private String imagen;

    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();

    public Categoria() {
        super();
    }

    public Categoria(Long id, String nombre, String descripcion) {
        super(id, false, null);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    void anexarProducto(Producto producto) {
        if (producto != null && !productos.contains(producto)) {
            productos.add(producto);
        }
    }

    void desanexarProducto(Producto producto) {
        productos.remove(producto);
    }

    public void agregarProducto(Producto producto) {
        if (producto != null) {
            producto.setCategoria(this);
        }
    }

    public void quitarProducto(Producto producto) {
        if (producto != null) {
            producto.setCategoria(null);
        }
    }

    @Override
    public String toString() {
        return "Categoria{id=" + id + ", nombre='" + nombre + "'}";
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
        Categoria categoria = (Categoria) o;
        return Objects.equals(nombre, categoria.nombre)
                && Objects.equals(descripcion, categoria.descripcion)
                && Objects.equals(imagen, categoria.imagen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nombre, descripcion, imagen);
    }
}
