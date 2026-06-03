package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.categoria.CategoriaCreate;
import com.tuuniversidad.foodstore.dto.categoria.CategoriaDto;
import com.tuuniversidad.foodstore.dto.categoria.CategoriaEdit;
import com.tuuniversidad.foodstore.exception.ResourceNotFoundException;
import com.tuuniversidad.foodstore.service.impl.CategoriaMapper;
import com.tuuniversidad.foodstore.model.Categoria;
import com.tuuniversidad.foodstore.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Transactional
    public CategoriaDto crear(CategoriaCreate dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        return categoriaMapper.toDto(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaDto> listarActivas() {
        return categoriaRepository.findByEliminadoFalse().stream()
                .map(categoriaMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDto buscarPorId(Long id) {
        return categoriaMapper.toDto(obtenerEntidad(id));
    }

    @Transactional
    public CategoriaDto actualizar(Long id, CategoriaEdit dto) {
        Categoria categoria = obtenerEntidad(id);
        categoriaMapper.updateEntity(categoria, dto);
        return categoriaMapper.toDto(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminarLogico(Long id) {
        Categoria categoria = obtenerEntidad(id);
        categoria.setEliminado(true);
        categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public Categoria obtenerEntidad(Long id) {
        return categoriaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }
}
