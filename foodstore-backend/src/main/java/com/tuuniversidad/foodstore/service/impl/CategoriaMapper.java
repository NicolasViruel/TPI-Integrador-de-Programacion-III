package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.categoria.CategoriaCreate;
import com.tuuniversidad.foodstore.dto.categoria.CategoriaDto;
import com.tuuniversidad.foodstore.dto.categoria.CategoriaEdit;
import com.tuuniversidad.foodstore.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaCreate dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setImagen(dto.getImagen());
        return categoria;
    }

    public void updateEntity(Categoria categoria, CategoriaEdit dto) {
        if (dto.getNombre() != null) {
            categoria.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            categoria.setDescripcion(dto.getDescripcion());
        }
        if (dto.getImagen() != null) {
            categoria.setImagen(dto.getImagen());
        }
    }

    public CategoriaDto toDto(Categoria categoria) {
        return CategoriaDto.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .imagen(categoria.getImagen())
                .cantidadProductos(categoria.getProductos().size())
                .build();
    }
}
