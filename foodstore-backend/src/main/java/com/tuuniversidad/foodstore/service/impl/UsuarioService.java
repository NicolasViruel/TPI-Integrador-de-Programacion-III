package com.tuuniversidad.foodstore.service.impl;

import com.tuuniversidad.foodstore.dto.auth.LoginRequest;
import com.tuuniversidad.foodstore.dto.auth.RegisterRequest;
import com.tuuniversidad.foodstore.dto.usuario.UsuarioCreate;
import com.tuuniversidad.foodstore.dto.usuario.UsuarioDto;
import com.tuuniversidad.foodstore.dto.usuario.UsuarioEdit;
import com.tuuniversidad.foodstore.exception.BadRequestException;
import com.tuuniversidad.foodstore.exception.ResourceNotFoundException;
import com.tuuniversidad.foodstore.exception.UnauthorizedException;
import com.tuuniversidad.foodstore.service.impl.UsuarioMapper;
import com.tuuniversidad.foodstore.model.Usuario;
import com.tuuniversidad.foodstore.model.enums.Rol;
import com.tuuniversidad.foodstore.repository.UsuarioRepository;
import com.tuuniversidad.foodstore.service.impl.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDto registrar(RegisterRequest request) {
        validarEmailUnico(request.getEmail());

        UsuarioCreate create = UsuarioCreate.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .mail(request.getEmail().trim().toLowerCase())
                .celular(request.getCelular())
                .contrasena(request.getPassword())
                .rol(Rol.USUARIO)
                .build();

        return crear(create);
    }

    @Transactional(readOnly = true)
    public UsuarioDto login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByMailAndEliminadoFalse(email)
                .orElseThrow(() -> new UnauthorizedException("Email o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getContrasena())) {
            throw new UnauthorizedException("Email o contraseña incorrectos");
        }

        return usuarioMapper.toDto(usuario);
    }

    @Transactional
    public UsuarioDto crear(UsuarioCreate dto) {
        validarEmailUnico(dto.getMail());

        Usuario usuario = usuarioMapper.toEntity(dto);
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.USUARIO);
        }
        usuario.setMail(usuario.getMail().trim().toLowerCase());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarActivos() {
        return usuarioRepository.findByEliminadoFalse().stream()
                .map(usuarioMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDto buscarPorId(Long id) {
        return usuarioMapper.toDto(obtenerEntidad(id));
    }

    @Transactional
    public UsuarioDto actualizar(Long id, UsuarioEdit dto) {
        Usuario usuario = obtenerEntidad(id);

        if (dto.getMail() != null) {
            String nuevoMail = dto.getMail().trim().toLowerCase();
            if (!nuevoMail.equals(usuario.getMail())) {
                validarEmailUnico(nuevoMail);
            }
        }

        usuarioMapper.updateEntity(usuario, dto);

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            if (dto.getContrasena().length() < 6) {
                throw new BadRequestException("La contraseña debe tener al menos 6 caracteres");
            }
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminarLogico(Long id) {
        Usuario usuario = obtenerEntidad(id);
        usuario.setEliminado(true);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerEntidad(Long id) {
        return usuarioRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    private void validarEmailUnico(String mail) {
        String normalized = mail.trim().toLowerCase();
        if (usuarioRepository.findByMailAndEliminadoFalse(normalized).isPresent()) {
            throw new BadRequestException("Ya existe un usuario con el email: " + normalized);
        }
    }
}
