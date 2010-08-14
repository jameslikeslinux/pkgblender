package com.thestaticvoid.blender.service;

import java.util.List;
import java.util.Map;

import javax.validation.Validator;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.GenericDao;
import com.thestaticvoid.blender.domain.Os;

@Service
public class AdministrationService {
	private Validator validator;
	private GenericDao genericDao;
	
	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setGenericDao(GenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
	@Transactional(readOnly = true)
	private boolean osNameExists(String name) {
		return genericDao.getByColumn(Os.class, "name", name) != null;
	}
	
	@Transactional(readOnly = true)
	private boolean osSlugExists(String slug) {
		return genericDao.getByColumn(Os.class, "slug", slug) != null;
	}
	
	public void createOs(OsDetails osDetails) {
		Map<String, String> errors = Utils.validate(validator, osDetails);
		
		if (osNameExists(osDetails.getName()))
			errors.put("name", "os.name.already.exists");
		
		if (osSlugExists(osDetails.getSlug()))
			errors.put("slug", "os.slug.already.exists");
		
		if (errors.size() > 0)
			throw new ValidationException(errors);
		
		Os os = new Os();
		BeanUtils.copyProperties(osDetails, os);
		os.setStatus(Os.Status.ADDING);
		genericDao.store(os);
		
		new PublisherCatalogReader(os, genericDao).start();
	}
	
	@Transactional(readOnly = true)
	public List<Os> getOses() {
		return genericDao.getAll(Os.class);
	}
}
