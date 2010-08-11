package com.thestaticvoid.blender.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "oses")
public class Os {
	public enum Status {
		ADDING, REFRESHING, FAILED, OK
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "slug", nullable = false, unique = true)
	private String slug;
	
	@Column(name = "publisher", nullable = false)
	private String publisher;
	
	@Column(name = "branch", nullable = false)
	private String branch;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@OneToMany(mappedBy = "os", cascade = CascadeType.ALL)
	private List<OsPackage> packages = new ArrayList<OsPackage>();

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<OsPackage> getPackages() {
		return packages;
	}

	public void setPackages(List<OsPackage> packages) {
		this.packages = packages;
	}
	
	public void addPackage(OsPackage pkg) {
		packages.add(pkg);
	}
}
