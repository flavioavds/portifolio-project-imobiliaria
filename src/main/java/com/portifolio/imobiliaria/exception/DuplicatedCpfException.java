package com.portifolio.imobiliaria.exception;

public class DuplicatedCpfException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DuplicatedCpfException(String message) {
        super(message);
    }
}