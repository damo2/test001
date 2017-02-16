package com.shwm.freshmallpos.util;

public class ExceptionUtil extends Exception {
	private String message;
	private static final long serialVersionUID = -707396355691365455L;

	public ExceptionUtil(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return message;
	}
}
