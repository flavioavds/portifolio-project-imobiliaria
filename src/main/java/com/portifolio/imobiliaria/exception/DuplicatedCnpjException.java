package com.portifolio.imobiliaria.exception;

public class DuplicatedCnpjException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DuplicatedCnpjException(String message) {
        super(message);
    }
}