package com.coderhouse.excepciones;

public class ResourceNotFoundsException extends RuntimeException {
    public ResourceNotFoundsException(String message) {
        super(message);
    }
}
