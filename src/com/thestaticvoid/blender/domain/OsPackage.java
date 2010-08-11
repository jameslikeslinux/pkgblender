package com.thestaticvoid.blender.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "os_packages")
public class OsPackage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "os_id")
	private Os os;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "fmri", nullable = false)
	private String fmri;
	
	@OneToMany(mappedBy = "osPackage", cascade = CascadeType.ALL)
	private List<LegacyPackage> legacyPackages = new ArrayList<LegacyPackage>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFmri() {
		return fmri;
	}

	public void setFmri(String fmri) {
		this.fmri = fmri;
	}

	public Os getOs() {
		return os;
	}

	public void setOs(Os os) {
		this.os = os;
	}

	public List<LegacyPackage> getLegacyPackages() {
		return legacyPackages;
	}

	public void setLegacyPackages(List<LegacyPackage> legacyPackages) {
		this.legacyPackages = legacyPackages;
	}
	
	public void addLegacyPackage(LegacyPackage legacyPackage) {
		legacyPackages.add(legacyPackage);
	}
	
	public boolean containsLegacyPackage(String name) {
		for (LegacyPackage legacyPackage : legacyPackages)
			if (legacyPackage.getName().equals(name))
				return true;
		
		return false;
	}
}
