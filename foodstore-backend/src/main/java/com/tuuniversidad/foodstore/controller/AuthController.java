package com.tuuniversidad.foodstore.controller;

import com.tuuniversidad.foodstore.dto.auth.LoginRequest;
import com.tuuniversidad.foodstore.dto.auth.RegisterRequest;
import com.tuuniversidad.foodstore.dto.usuario.UsuarioDto;
import com.tuuniversidad.foodstore.service.impl.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public UsuarioDto login(@Valid @RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDto> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(request));
    }
}
