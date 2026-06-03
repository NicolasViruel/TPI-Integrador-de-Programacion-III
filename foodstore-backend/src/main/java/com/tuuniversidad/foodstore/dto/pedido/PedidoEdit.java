package com.tuuniversidad.foodstore.dto.pedido;

import com.tuuniversidad.foodstore.model.enums.Estado;
import com.tuuniversidad.foodstore.model.enums.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEdit {

    private LocalDate fecha;
    private Estado estado;
    private FormaPago formaPago;
    private Long usuarioId;
}
