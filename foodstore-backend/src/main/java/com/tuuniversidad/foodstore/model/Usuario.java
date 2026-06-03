package com.tuuniversidad.foodstore.model;

import com.tuuniversidad.foodstore.model.enums.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class Usuario extends Base {

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String apellido;

    @Column(nullable = false, unique = true, length = 255)
    private String mail;

    @Column(length = 40)
    private String celular;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos = new ArrayList<>();

    public Usuario() {
        super();
    }

    public Usuario(Long id, String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) {
        super(id, false, null);
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public void addPedido(Pedido pedido) {
        if (pedido == null) {
            return;
        }
        pedidos.add(pedido);
        pedido.setUsuario(this);
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombreCompleto='" + getNombreCompleto() + "', mail='" + mail + "', rol=" + rol + "}";
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
        Usuario usuario = (Usuario) o;
        return Objects.equals(nombre, usuario.nombre)
                && Objects.equals(apellido, usuario.apellido)
                && Objects.equals(mail, usuario.mail)
                && Objects.equals(celular, usuario.celular)
                && Objects.equals(contrasena, usuario.contrasena)
                && rol == usuario.rol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nombre, apellido, mail, celular, contrasena, rol);
    }
}
