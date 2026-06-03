package com.tuuniversidad.foodstore.controller;

import com.tuuniversidad.foodstore.dto.pedido.PedidoCreateRequest;
import com.tuuniversidad.foodstore.dto.pedido.PedidoDto;
import com.tuuniversidad.foodstore.dto.pedido.PedidoEdit;
import com.tuuniversidad.foodstore.dto.pedido.PedidoStatusUpdate;
import com.tuuniversidad.foodstore.service.impl.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoDto> listar() {
        return pedidoService.listarActivos();
    }

    @GetMapping("/user/{userId}")
    public List<PedidoDto> listarPorUsuario(@PathVariable Long userId) {
        return pedidoService.listarPorUsuario(userId);
    }

    @GetMapping("/{id}")
    public PedidoDto buscar(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<PedidoDto> crear(@RequestBody PedidoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(request));
    }

    @PatchMapping("/{id}/status")
    public PedidoDto actualizarEstado(@PathVariable Long id, @RequestBody PedidoStatusUpdate dto) {
        return pedidoService.actualizarEstado(id, dto);
    }

    @PutMapping("/{id}")
    public PedidoDto actualizar(@PathVariable Long id, @RequestBody PedidoEdit dto) {
        return pedidoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }
}
