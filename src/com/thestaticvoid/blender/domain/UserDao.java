package com.thestaticvoid.blender.domain;

public interface UserDao {
	public User getByEmail(String email);
}