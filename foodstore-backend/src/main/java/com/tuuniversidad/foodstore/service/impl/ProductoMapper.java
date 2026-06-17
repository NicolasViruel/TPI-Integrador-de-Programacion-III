package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.producto.ProductoCreate;
import com.tuuniversidad.foodstore.dto.producto.ProductoDto;
import com.tuuniversidad.foodstore.dto.producto.ProductoEdit;
import com.tuuniversidad.foodstore.model.Categoria;
import com.tuuniversidad.foodstore.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoCreate dto, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setImagen(dto.getImagen());
        producto.setDisponible(dto.getDisponible());
        if (categoria != null) {
            categoria.agregarProducto(producto);
        }
        return producto;
    }

    public void updateEntity(Producto producto, ProductoEdit dto, Categoria categoria) {
        if (dto.getNombre() != null) {
            producto.setNombre(dto.getNombre());
        }
        if (dto.getPrecio() != null) {
            producto.setPrecio(dto.getPrecio());
        }
        if (dto.getDescripcion() != null) {
            producto.setDescripcion(dto.getDescripcion());
        }
        producto.setStock(dto.getStock());
        if (dto.getImagen() != null) {
            producto.setImagen(dto.getImagen());
        }
        if (dto.getDisponible() != null) {
            producto.setDisponible(dto.getDisponible());
        }
        if (categoria != null) {
            producto.setCategoria(categoria);
        }
    }

    public ProductoDto toDto(Producto producto) {
        return ProductoDto.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .imagen(producto.getImagen())
                .disponible(producto.getDisponible())
                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
                .categoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null)
                .updatedAt(producto.getUpdatedAt())
                .build();
    }
}
