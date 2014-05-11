package com.willydupreez.prototype.structure;

public class LauncherException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LauncherException(String message, Throwable cause) {
		super(message, cause);
	}

	public LauncherException(String message) {
		super(message);
	}

}
