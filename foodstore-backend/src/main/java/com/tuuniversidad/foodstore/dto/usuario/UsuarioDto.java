package com.tuuniversidad.foodstore.dto.usuario;

import com.tuuniversidad.foodstore.model.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private Rol rol;
    private String nombreCompleto;
}
