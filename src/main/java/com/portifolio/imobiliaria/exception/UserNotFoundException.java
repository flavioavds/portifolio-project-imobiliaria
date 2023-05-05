package com.portifolio.imobiliaria.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(UUID id) {
        super("User not found with id " + id);
    }
}

