package com.thestaticvoid.blender.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EmailValidationTokenDaoImpl implements EmailValidationTokenDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void store(EmailValidationToken emailValidationToken) {
		entityManager.persist(emailValidationToken);
		entityManager.flush();
	}
	
	@Transactional
	public void remove(EmailValidationToken emailValidationToken) {
		entityManager.remove(emailValidationToken);
	}
}