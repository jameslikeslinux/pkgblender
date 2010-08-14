package com.thestaticvoid.blender.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.GenericDao;
import com.thestaticvoid.blender.domain.LegacyPackage;
import com.thestaticvoid.blender.domain.Os;
import com.thestaticvoid.blender.domain.OsPackage;

public class PublisherCatalogReader extends Thread {
	private Os os;
	private GenericDao genericDao;
	
	public PublisherCatalogReader(Os os, GenericDao genericDao) {
		this.os = os;
		this.genericDao = genericDao;
	}
	
	@Transactional
	public void run() {
		os = genericDao.get(Os.class, os.getId());
		
		try {
			BufferedReader catalogReader = new BufferedReader(new InputStreamReader(new URL(os.getPublisher() + "/catalog/0/").openStream()));
			
			String line;
			Pattern pkgPattern = Pattern.compile("^V (pkg:/((.*)@.*-(" + Pattern.quote(os.getBranch()) + "):.*))$");
			Pattern legacyPattern = Pattern.compile("^legacy.*pkg=(\\S*).*$");
			
			while ((line = catalogReader.readLine()) != null) {
				Matcher pkgMatch = pkgPattern.matcher(line);
				if (pkgMatch.matches()) {
					Logger.getLogger(getClass()).info("Adding package " + pkgMatch.group(3));
					
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
		} catch (Exception e) {
			Logger.getLogger(getClass()).debug("Exception while reading catalog at " + os.getPublisher(), e);
			os.setStatus(Os.Status.FAILED);
		}
		
		os.setStatus(Os.Status.OK);
		Logger.getLogger(getClass()).info("Completed adding packages from publisher.");
	}
}
