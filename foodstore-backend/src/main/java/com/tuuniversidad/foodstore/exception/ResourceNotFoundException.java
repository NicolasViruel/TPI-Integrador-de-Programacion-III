package com.tuuniversidad.foodstore.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entityName, Long id) {
        super("Entidad con id " + id + " no encontrado");
    }
}
