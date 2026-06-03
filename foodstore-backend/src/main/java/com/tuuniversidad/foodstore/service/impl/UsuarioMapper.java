package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.usuario.UsuarioCreate;
import com.tuuniversidad.foodstore.dto.usuario.UsuarioDto;
import com.tuuniversidad.foodstore.dto.usuario.UsuarioEdit;
import com.tuuniversidad.foodstore.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioCreate dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setMail(dto.getMail());
        usuario.setCelular(dto.getCelular());
        usuario.setContrasena(dto.getContrasena());
        usuario.setRol(dto.getRol());
        return usuario;
    }

    public void updateEntity(Usuario usuario, UsuarioEdit dto) {
        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            usuario.setApellido(dto.getApellido());
        }
        if (dto.getMail() != null) {
            usuario.setMail(dto.getMail());
        }
        if (dto.getCelular() != null) {
            usuario.setCelular(dto.getCelular());
        }
        if (dto.getContrasena() != null) {
            usuario.setContrasena(dto.getContrasena());
        }
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol());
        }
    }

    public UsuarioDto toDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .mail(usuario.getMail())
                .celular(usuario.getCelular())
                .rol(usuario.getRol())
                .nombreCompleto(usuario.getNombreCompleto())
                .build();
    }
}
