package com.tuuniversidad.foodstore.controller;

import com.tuuniversidad.foodstore.dto.producto.ProductoCreate;
import com.tuuniversidad.foodstore.dto.producto.ProductoDto;
import com.tuuniversidad.foodstore.dto.producto.ProductoEdit;
import com.tuuniversidad.foodstore.service.impl.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDto> listar() {
        return productoService.listarActivos();
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductoDto> listarPorCategoria(@PathVariable Long categoryId) {
        return productoService.listarPorCategoria(categoryId);
    }

    @GetMapping("/{id}")
    public ProductoDto buscar(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProductoDto> crear(@RequestBody ProductoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ProductoDto actualizar(@PathVariable Long id, @RequestBody ProductoEdit dto) {
        return productoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }
}
