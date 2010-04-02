package com.thestaticvoid.blender.domain;

public interface EmailValidationTokenDao {
	public void store(EmailValidationToken emailValidationToken);
	public void remove(EmailValidationToken emailValidationToken);
}
