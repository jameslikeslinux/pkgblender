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
		Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=?1");
		query.setParameter(1, username);
		
		try {
			return (User) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.email=?1");
		query.setParameter(1, email);
		
		try {
			for (Object result : query.getResultList()) {
				User user = (User) result;
				if (user.isEmailValid())
					return user;
			}
		} catch (NoResultException nre) {
			// empty
		}
		
		return null;
	}
	
	@Transactional(readOnly = true)
	public User get(int id) {
		return entityManager.find(User.class, id);
	}
	
	@Transactional
	public void store(User user) {
		entityManager.persist(user);
		entityManager.flush();
	}
	
	@Transactional(readOnly = true)
	public void refresh(User user) {
		entityManager.refresh(user);
	}
}