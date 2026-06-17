package com.tuuniversidad.foodstore.dto.detallePedido;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoCreate {

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int cantidad;

    @NotNull(message = "El idProducto es obligatorio")
    @JsonAlias({"productoId", "idProducto"})
    private Long idProducto;
}
