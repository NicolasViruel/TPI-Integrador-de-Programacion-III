package com.tuuniversidad.foodstore.dto.detallePedido;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoCreate {

    private int cantidad;

    @JsonAlias({"productoId", "idProducto"})
    private Long idProducto;
}
