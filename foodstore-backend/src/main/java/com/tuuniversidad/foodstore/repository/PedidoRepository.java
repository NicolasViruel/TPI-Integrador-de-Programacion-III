package com.tuuniversidad.foodstore.repository;

import com.tuuniversidad.foodstore.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEliminadoFalse();

    List<Pedido> findByEliminadoFalseOrderByFechaDesc();

    Optional<Pedido> findByIdAndEliminadoFalse(Long id);

    List<Pedido> findByUsuarioIdAndEliminadoFalse(Long usuarioId);

    List<Pedido> findByUsuarioIdAndEliminadoFalseOrderByFechaDesc(Long usuarioId);
}
