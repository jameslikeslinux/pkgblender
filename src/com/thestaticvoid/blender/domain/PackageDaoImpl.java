package com.thestaticvoid.blender.domain;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PackageDaoImpl implements PackageDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	public Package getByPackageName(String packageName) {
		Query query = entityManager.createQuery("SELECT p FROM Package p WHERE p.name=?1");
		query.setParameter(1, packageName);
		
		try {
			return (Package) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	@Transactional
	public void store(Package pkg) {
		entityManager.persist(pkg);
		entityManager.flush();
	}
}
