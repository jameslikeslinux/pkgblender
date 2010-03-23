package com.thestaticvoid.blender.web;

import com.thestaticvoid.blender.service.RegistrationDetails;

public class RegistrationForm extends RegistrationDetails {
	private String passwordConfirmation;
	private String reCaptcha;

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getReCaptcha() {
		return reCaptcha;
	}

	public void setReCaptcha(String reCaptcha) {
		this.reCaptcha = reCaptcha;
	}
}
