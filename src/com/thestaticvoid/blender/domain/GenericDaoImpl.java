package com.thestaticvoid.blender.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GenericDaoImpl implements GenericDao {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> T getByColumn(Class<T> entityType, String column, Object parameter) {
		String entityName = entityType.getName();
		entityName = entityName.substring(entityName.lastIndexOf('.') + 1);
		
		Query query = entityManager.createQuery("SELECT t FROM " + entityName + " t WHERE t." + column + "=?1");
		query.setParameter(1, parameter);
		
		try {
			return (T) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> getAll(Class<T> entityType) {
		String entityName = entityType.getName();
		entityName = entityName.substring(entityName.lastIndexOf('.') + 1);
		Query query = entityManager.createQuery("SELECT t FROM " + entityName + " t");
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public <T> T get(Class<T> entityType, int id) {
		return entityManager.find(entityType, id);
	}

	@Transactional
	public void store(Object entity) {
		entityManager.persist(entity);
		entityManager.flush();
	}

	@Transactional(readOnly = true)
	public void refresh(Object entity) {
		entityManager.refresh(entity);
	}

	@Transactional
	public void remove(Object entity) {
		entityManager.remove(entity);
	}
}