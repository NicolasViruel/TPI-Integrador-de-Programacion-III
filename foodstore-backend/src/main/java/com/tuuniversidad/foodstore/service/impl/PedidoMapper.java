package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.detallePedido.DetallePedidoDto;
import com.tuuniversidad.foodstore.dto.pedido.PedidoDto;
import com.tuuniversidad.foodstore.dto.pedido.PedidoEdit;
import com.tuuniversidad.foodstore.model.Pedido;
import com.tuuniversidad.foodstore.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public void updateEntity(Pedido pedido, PedidoEdit dto, Usuario usuario) {
        if (dto.getFecha() != null) {
            pedido.setFecha(dto.getFecha());
        }
        if (dto.getEstado() != null) {
            pedido.setEstado(dto.getEstado());
        }
        if (dto.getFormaPago() != null) {
            pedido.setFormaPago(dto.getFormaPago());
        }
        if (usuario != null) {
            pedido.setUsuario(usuario);
        }
    }

    public PedidoDto toDto(Pedido pedido) {
        List<DetallePedidoDto> detalles = pedido.getDetalles().stream()
                .map(d -> DetallePedidoDto.builder()
                        .id(d.getId())
                        .cantidad(d.getCantidad())
                        .subtotal(d.getSubtotal())
                        .productoId(d.getProducto().getId())
                        .productoNombre(d.getProducto().getNombre())
                        .build())
                .toList();

        double subtotal = pedido.calcularSubtotal().doubleValue();

        return PedidoDto.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .formaPago(pedido.getFormaPago())
                .usuarioId(pedido.getUsuario() != null ? pedido.getUsuario().getId() : null)
                .usuarioNombre(pedido.getUsuario() != null ? pedido.getUsuario().getNombreCompleto() : null)
                .telefono(pedido.getTelefono())
                .direccion(pedido.getDireccion())
                .costoEnvio(pedido.getCostoEnvio())
                .subtotal(subtotal)
                .detalles(detalles)
                .build();
    }
}
