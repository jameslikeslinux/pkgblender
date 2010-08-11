package com.thestaticvoid.blender.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "legacy_packages")
public class LegacyPackage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "os_package_id")
	private OsPackage osPackage;
	
	@Column(name = "name", nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OsPackage getOsPackage() {
		return osPackage;
	}

	public void setOsPackage(OsPackage osPackage) {
		this.osPackage = osPackage;
	}
}
