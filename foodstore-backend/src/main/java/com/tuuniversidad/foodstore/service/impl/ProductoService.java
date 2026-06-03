package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.producto.ProductoCreate;
import com.tuuniversidad.foodstore.dto.producto.ProductoDto;
import com.tuuniversidad.foodstore.dto.producto.ProductoEdit;
import com.tuuniversidad.foodstore.exception.BadRequestException;
import com.tuuniversidad.foodstore.exception.ResourceNotFoundException;
import com.tuuniversidad.foodstore.service.impl.ProductoMapper;
import com.tuuniversidad.foodstore.model.Categoria;
import com.tuuniversidad.foodstore.model.Producto;
import com.tuuniversidad.foodstore.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepository,
                           ProductoMapper productoMapper,
                           CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.categoriaService = categoriaService;
    }

    @Transactional
    public ProductoDto crear(ProductoCreate dto) {
        Categoria categoria = dto.getCategoriaId() != null
                ? categoriaService.obtenerEntidad(dto.getCategoriaId())
                : null;
        Producto producto = productoMapper.toEntity(dto, categoria);
        return productoMapper.toDto(productoRepository.save(producto));
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarActivos() {
        return productoRepository.findByEliminadoFalse().stream()
                .map(productoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarPorCategoria(Long categoriaId) {
        categoriaService.obtenerEntidad(categoriaId);
        return productoRepository.findByCategoriaIdAndEliminadoFalse(categoriaId).stream()
                .map(productoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoDto buscarPorId(Long id) {
        return productoMapper.toDto(obtenerEntidad(id));
    }

    @Transactional
    public ProductoDto actualizar(Long id, ProductoEdit dto) {
        Producto producto = obtenerEntidad(id);
        Categoria categoria = dto.getCategoriaId() != null
                ? categoriaService.obtenerEntidad(dto.getCategoriaId())
                : null;
        productoMapper.updateEntity(producto, dto, categoria);
        return productoMapper.toDto(productoRepository.save(producto));
    }

    @Transactional
    public void eliminarLogico(Long id) {
        Producto producto = obtenerEntidad(id);
        producto.setEliminado(true);
        productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public Producto obtenerEntidad(Long id) {
        return productoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    @Transactional
    public void reducirStock(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a 0");
        }
        if (!producto.estaDisponibleParaVenta()) {
            throw new BadRequestException("El producto '" + producto.getNombre() + "' no está disponible para la venta");
        }
        if (producto.getStock() < cantidad) {
            throw new BadRequestException(
                    "Stock insuficiente para '" + producto.getNombre()
                            + "'. Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }
}
