package com.tuuniversidad.foodstore.repository;

import com.tuuniversidad.foodstore.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByEliminadoFalse();

    Optional<Categoria> findByIdAndEliminadoFalse(Long id);
}
