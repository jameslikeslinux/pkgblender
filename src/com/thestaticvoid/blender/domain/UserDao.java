package com.thestaticvoid.blender.domain;

public interface UserDao {
	public User getByUsername(String username);
	public User getByEmail(String email);
	public User get(int id);
	public void store(User user);
	public void refresh(User user);
}