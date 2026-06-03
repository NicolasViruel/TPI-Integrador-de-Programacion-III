package com.tuuniversidad.foodstore.repository;

import com.tuuniversidad.foodstore.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByEliminadoFalse();

    Optional<Usuario> findByIdAndEliminadoFalse(Long id);

    Optional<Usuario> findByMailAndEliminadoFalse(String mail);
}
