package com.tuuniversidad.foodstore.dto.pedido;

import com.tuuniversidad.foodstore.dto.detallePedido.DetallePedidoCreate;
import com.tuuniversidad.foodstore.model.enums.Estado;
import com.tuuniversidad.foodstore.model.enums.FormaPago;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    @NotNull(message = "La forma de pago es obligatoria")
    private FormaPago formaPago;

    @NotNull(message = "El idUsuario es obligatorio")
    @JsonAlias({"usuarioId", "idUsuario"})
    private Long idUsuario;

    @NotEmpty(message = "Se requiere al menos un detalle")
    @Valid
    @JsonAlias({"detalles", "detallePedido"})
    private List<DetallePedidoCreate> detallePedido;

    private String telefono;
    private String direccion;
    private Double costoEnvio;
}
