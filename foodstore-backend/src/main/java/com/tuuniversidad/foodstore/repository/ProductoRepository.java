package com.tuuniversidad.foodstore.repository;

import com.tuuniversidad.foodstore.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByEliminadoFalse();

    Optional<Producto> findByIdAndEliminadoFalse(Long id);

    List<Producto> findByCategoriaIdAndEliminadoFalse(Long categoriaId);
}
