package com.thestaticvoid.blender.domain;

import java.util.List;

public interface GenericDao {
	public <T> T getByColumn(Class<T> entityType, String column, Object parameter);
	public <T> List<T> getAll(Class<T> entityType);
	public <T> T get(Class<T> entityType, int id);
	public void store(Object entity);
	public void refresh(Object entity);
	public void remove(Object entity);
}