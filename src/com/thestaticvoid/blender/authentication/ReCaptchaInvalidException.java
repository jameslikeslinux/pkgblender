package com.thestaticvoid.blender.authentication;

import org.springframework.security.core.AuthenticationException;

public class ReCaptchaInvalidException extends AuthenticationException {
	private static final long serialVersionUID = -3539568855893303256L;

	public ReCaptchaInvalidException() {
		super("Invalid or no reCAPTCHA submitted.");
	}
}
