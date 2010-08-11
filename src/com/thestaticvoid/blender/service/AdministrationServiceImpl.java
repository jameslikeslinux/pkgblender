package com.thestaticvoid.blender.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Validator;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.GenericDao;
import com.thestaticvoid.blender.domain.LegacyPackage;
import com.thestaticvoid.blender.domain.Os;
import com.thestaticvoid.blender.domain.OsPackage;

@Service
public class AdministrationServiceImpl implements AdministrationService {
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
	
	@Transactional
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
		
		try {
			BufferedReader catalogReader = new BufferedReader(new InputStreamReader(new URL(os.getPublisher() + "/catalog/0/").openStream()));
			
			String line;
			Pattern pkgPattern = Pattern.compile("^V (pkg:/((.*)@.*-(" + Pattern.quote(os.getBranch()) + "):.*))$");
			Pattern legacyPattern = Pattern.compile("^legacy.*pkg=(\\S*).*$");
			
			while ((line = catalogReader.readLine()) != null) {
				Matcher pkgMatch = pkgPattern.matcher(line);
				if (pkgMatch.matches()) {
					OsPackage osPackage = new OsPackage();
					osPackage.setName(pkgMatch.group(3));
					osPackage.setFmri(pkgMatch.group(1));
					osPackage.setOs(os);
					os.addPackage(osPackage);
					
					URI manifestUri = new URI(os.getPublisher() + "/manifest/0/" + pkgMatch.group(2));
					BufferedReader manifestReader = new BufferedReader(new InputStreamReader(manifestUri.toURL().openStream()));
					
					while ((line = manifestReader.readLine()) != null) {
						Matcher legacyMatch = legacyPattern.matcher(line);
						if (legacyMatch.matches()) {
							String pkgName = legacyMatch.group(1);
							if (!osPackage.containsLegacyPackage(pkgName)) {
								LegacyPackage legacyPackage = new LegacyPackage();
								legacyPackage.setName(pkgName);
								legacyPackage.setOsPackage(osPackage);
								osPackage.addLegacyPackage(legacyPackage);
							}
						}
					}
					
					manifestReader.close();
				}
			}
			
			catalogReader.close();
		} catch (IOException e) {
			errors.put("publisher", "os.invalid.publisher");
			throw new ValidationException(errors);
		} catch (URISyntaxException e) {
			errors.put("publisher", "os.invalid.publisher");
			throw new ValidationException(errors);
		}
		
		genericDao.store(os);
	}
}
