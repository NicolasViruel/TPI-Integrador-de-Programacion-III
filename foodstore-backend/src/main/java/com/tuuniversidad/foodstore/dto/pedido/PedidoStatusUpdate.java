package com.tuuniversidad.foodstore.dto.pedido;

import com.tuuniversidad.foodstore.model.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoStatusUpdate {

    private Estado estado;
}
