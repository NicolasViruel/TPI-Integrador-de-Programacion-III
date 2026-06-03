package com.tuuniversidad.foodstore.dto.pedido;

import com.tuuniversidad.foodstore.dto.detallePedido.DetallePedidoDto;
import com.tuuniversidad.foodstore.model.enums.Estado;
import com.tuuniversidad.foodstore.model.enums.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDto {

    private Long id;
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Long usuarioId;
    private String usuarioNombre;
    private String telefono;
    private String direccion;
    private Double costoEnvio;
    private Double subtotal;
    private List<DetallePedidoDto> detalles;
}
