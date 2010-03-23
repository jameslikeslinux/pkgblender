package com.thestaticvoid.blender.domain;

public interface UserDao {
	public User getByUsername(String username);
	public User getByEmail(String email);
	public void store(User user);
}
