package com.tuuniversidad.foodstore.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEdit {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precio;

    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    private String imagen;

    private Boolean disponible;

    private Long categoriaId;
}
