package com.thestaticvoid.blender.domain;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDao {
	@PersistenceContext
	private EntityManager entityManager;
	
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
}