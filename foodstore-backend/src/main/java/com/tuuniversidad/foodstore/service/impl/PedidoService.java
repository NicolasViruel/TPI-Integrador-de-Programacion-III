package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.pedido.PedidoCreateRequest;
import com.tuuniversidad.foodstore.dto.pedido.PedidoDto;
import com.tuuniversidad.foodstore.dto.pedido.PedidoEdit;
import com.tuuniversidad.foodstore.dto.pedido.PedidoStatusUpdate;
import com.tuuniversidad.foodstore.exception.BadRequestException;
import com.tuuniversidad.foodstore.exception.ResourceNotFoundException;
import com.tuuniversidad.foodstore.service.impl.PedidoMapper;
import com.tuuniversidad.foodstore.model.Pedido;
import com.tuuniversidad.foodstore.model.Producto;
import com.tuuniversidad.foodstore.model.Usuario;
import com.tuuniversidad.foodstore.model.enums.Estado;
import com.tuuniversidad.foodstore.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         PedidoMapper pedidoMapper,
                         UsuarioService usuarioService,
                         ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @Transactional
    public PedidoDto crear(PedidoCreateRequest request) {
        if (request.getIdUsuario() == null) {
            throw new BadRequestException("El idUsuario es obligatorio");
        }
        if (request.getEstado() == null) {
            throw new BadRequestException("El estado es obligatorio");
        }
        if (request.getFormaPago() == null) {
            throw new BadRequestException("La forma de pago es obligatoria");
        }
        if (request.getDetallePedido() == null || request.getDetallePedido().isEmpty()) {
            throw new BadRequestException("Se requiere al menos un detalle de pedido");
        }

        Usuario usuario = usuarioService.obtenerEntidad(request.getIdUsuario());

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(request.getEstado());
        pedido.setFormaPago(request.getFormaPago());
        pedido.setUsuario(usuario);
        if (request.getTelefono() != null && !request.getTelefono().isBlank()) {
            pedido.setTelefono(request.getTelefono().trim());
        } else if (usuario.getCelular() != null) {
            pedido.setTelefono(usuario.getCelular());
        }
        if (request.getDireccion() != null && !request.getDireccion().isBlank()) {
            pedido.setDireccion(request.getDireccion().trim());
        }
        BigDecimal costoEnvio = request.getCostoEnvio() != null
                ? BigDecimal.valueOf(request.getCostoEnvio())
                : new BigDecimal("500");
        pedido.setCostoEnvio(costoEnvio.doubleValue());
        usuario.addPedido(pedido);

        request.getDetallePedido().forEach(detalle -> {
            if (detalle.getIdProducto() == null) {
                throw new BadRequestException("El idProducto es obligatorio en cada detalle");
            }
            if (detalle.getCantidad() <= 0) {
                throw new BadRequestException("La cantidad debe ser mayor a 0");
            }

            Producto producto = productoService.obtenerEntidad(detalle.getIdProducto());
            productoService.reducirStock(producto, detalle.getCantidad());
            pedido.addDetallePedido(detalle.getCantidad(), producto);
        });

        pedido.calcularTotal();
        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoDto> listarActivos() {
        return pedidoRepository.findByEliminadoFalseOrderByFechaDesc().stream()
                .map(pedidoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoDto> listarPorUsuario(Long usuarioId) {
        usuarioService.obtenerEntidad(usuarioId);
        return pedidoRepository.findByUsuarioIdAndEliminadoFalseOrderByFechaDesc(usuarioId).stream()
                .map(pedidoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoDto buscarPorId(Long id) {
        return pedidoMapper.toDto(obtenerEntidad(id));
    }

    @Transactional
    public PedidoDto actualizarEstado(Long id, PedidoStatusUpdate dto) {
        Pedido pedido = obtenerEntidad(id);
        if (dto.getEstado() == null) {
            throw new BadRequestException("El estado es obligatorio");
        }
        pedido.setEstado(dto.getEstado());
        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoDto actualizar(Long id, PedidoEdit dto) {
        Pedido pedido = obtenerEntidad(id);
        Usuario usuario = dto.getUsuarioId() != null
                ? usuarioService.obtenerEntidad(dto.getUsuarioId())
                : null;
        pedidoMapper.updateEntity(pedido, dto, usuario);
        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    @Transactional
    public void eliminarLogico(Long id) {
        Pedido pedido = obtenerEntidad(id);
        pedido.setEliminado(true);
        pedidoRepository.save(pedido);
    }

    private Pedido obtenerEntidad(Long id) {
        return pedidoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }
}
