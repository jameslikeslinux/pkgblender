package com.thestaticvoid.blender;

import java.util.List;

public interface PersonDao {
	public void store(Person person);
	public void delete(int id);
	public Person findById(int id);
	public List<Person> findAll();
}
