package com.thestaticvoid.blender.domain;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public class RepositoryDaoImpl implements RepositoryDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	public Repository getByName(String name) {
		Query query = entityManager.createQuery("SELECT r FROM Repository r WHERE r.name=?1");
		query.setParameter(1, name);
		
		try {
			return (Repository) query.getSingleResult();
		} catch (NoResultException nre) {
			Repository repository = new Repository();
			repository.setName(name);
			store(repository);
			return repository;
		}
	}
	
	@Transactional
	public void store(Repository repository) {
		entityManager.persist(repository);
		entityManager.flush();
	}
}
