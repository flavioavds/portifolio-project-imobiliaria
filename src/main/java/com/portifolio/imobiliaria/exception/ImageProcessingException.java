package com.portifolio.imobiliaria.exception;

public class ImageProcessingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ImageProcessingException(String message) {
        super(message);
    }

    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
