package com.tuuniversidad.foodstore.dto.usuario;

import com.tuuniversidad.foodstore.model.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEdit {

    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @Email(message = "Formato de email invalido")
    private String mail;

    @Size(max = 20, message = "El celular no puede superar 20 caracteres")
    private String celular;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    private Rol rol;
}
