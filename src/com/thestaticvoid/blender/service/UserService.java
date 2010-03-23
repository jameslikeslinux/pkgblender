package com.thestaticvoid.blender.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	public boolean userExists(String username);
	public boolean emailRegistered(String email);
	public void registerUser(RegistrationDetails details, boolean validateOnly);
}