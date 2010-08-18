package com.thestaticvoid.blender.domain;

import java.util.List;

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
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Os> getOsesContainingLegacy(String packageName) {
		Query query = entityManager.createQuery("SELECT o FROM Os o INNER JOIN o.packages p INNER JOIN p.legacyPackages l WHERE l.name=?1");
		query.setParameter(1, packageName);
		return query.getResultList();
	}
	
	public Package getPackageContainingLegacy(String packageName) {
		return null;
	}
	
	@Transactional(readOnly = true)
	public Branch getBranch(String packageName, String branchName) {
		Query query = entityManager.createQuery("SELECT b FROM Branch b WHERE b.name=?1 AND b.pkg.name=?2");
		query.setParameter(1, branchName);
		query.setParameter(2, packageName);
		
		try {
			return (Branch) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
}
