package com.tuuniversidad.foodstore.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private int cantidadProductos;
}
