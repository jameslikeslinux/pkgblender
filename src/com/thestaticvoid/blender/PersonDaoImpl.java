package com.thestaticvoid.blender;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PersonDaoImpl implements PersonDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void store(Person person) {
		entityManager.merge(person);
	}
	
	@Transactional
	public void delete(int id) {
		Person person = findById(id);
		entityManager.remove(person);
	}
	
	@Transactional(readOnly = true)
	public Person findById(int id) {
		return entityManager.find(Person.class, id);
	}
	
	@Transactional(readOnly = true)
	public List<Person> findAll() {
		Query query = entityManager.createQuery("from Person");
		return query.getResultList();
	}
}