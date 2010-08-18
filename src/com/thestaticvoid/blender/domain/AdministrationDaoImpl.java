package com.thestaticvoid.blender.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AdministrationDaoImpl implements AdministrationDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Os> getOkOses() {
		Query query = entityManager.createQuery("SELECT o FROM Os o WHERE o.status=?1");
		query.setParameter(1, Os.Status.OK);
		return query.getResultList();
	}
}