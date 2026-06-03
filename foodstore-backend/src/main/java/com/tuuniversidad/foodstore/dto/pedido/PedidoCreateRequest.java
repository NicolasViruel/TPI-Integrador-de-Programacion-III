package com.tuuniversidad.foodstore.dto.pedido;

import com.tuuniversidad.foodstore.dto.detallePedido.DetallePedidoCreate;
import com.tuuniversidad.foodstore.model.enums.Estado;
import com.tuuniversidad.foodstore.model.enums.FormaPago;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreateRequest {

    private Estado estado;
    private FormaPago formaPago;

    @JsonAlias({"usuarioId", "idUsuario"})
    private Long idUsuario;

    @JsonAlias({"detalles", "detallePedido"})
    private List<DetallePedidoCreate> detallePedido;

    private String telefono;
    private String direccion;
    private Double costoEnvio;
}
