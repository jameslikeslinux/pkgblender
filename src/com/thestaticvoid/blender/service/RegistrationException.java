package com.thestaticvoid.blender.service;

import java.util.Map;

public class RegistrationException extends RuntimeException {
	private static final long serialVersionUID = -7388481871177315932L;

	private Map<String, String> errors;
	
	public RegistrationException(Map<String, String> errors) {
		this.errors = errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}