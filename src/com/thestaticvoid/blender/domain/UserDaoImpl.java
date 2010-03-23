package com.thestaticvoid.blender.domain;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDaoImpl implements UserDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	public User getByUsername(String username) {
		Query query = entityManager.createQuery("from User where username=?1");
		query.setParameter(1, username);
		
		try {
			return (User) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		Query query = entityManager.createQuery("from User where email=?1 and emailValid=true");
		query.setParameter(1, email);
		
		try {
			return (User) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	@Transactional
	public void store(User user) {
		entityManager.merge(user);
	}
}