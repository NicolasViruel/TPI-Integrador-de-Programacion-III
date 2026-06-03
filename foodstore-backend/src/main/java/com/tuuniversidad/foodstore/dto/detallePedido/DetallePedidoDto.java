package com.tuuniversidad.foodstore.dto.detallePedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDto {

    private Long id;
    private int cantidad;
    private Double subtotal;
    private Long productoId;
    private String productoNombre;
}
