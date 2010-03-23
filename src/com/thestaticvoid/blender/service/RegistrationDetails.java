package com.thestaticvoid.blender.service;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.thestaticvoid.blender.constraints.Password;

public class RegistrationDetails {
	@NotEmpty
	@Pattern(regexp = "^([a-z][a-z0-9_-]{1,6}[a-z0-9])?$", message = "invalid.username")
	private String username;
	
	@NotEmpty
	@Password
	private String password;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	private String name;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RegistrationDetails [email=" + email + ", name=" + name
				+ ", password=" + password + ", username=" + username + "]";
	}
}
