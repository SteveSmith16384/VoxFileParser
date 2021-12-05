package com.scs.voxlib;

public class InvalidVoxException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidVoxException(String message) {
        super(message);
    }

    public InvalidVoxException(String message, Throwable cause) {
        super(message, cause);
    }
}
