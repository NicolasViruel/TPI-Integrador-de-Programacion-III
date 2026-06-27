package com.tuuniversidad.foodstore.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private Boolean disponible;
    private Long categoriaId;
    private String categoriaNombre;
    private LocalDateTime updatedAt;
}
