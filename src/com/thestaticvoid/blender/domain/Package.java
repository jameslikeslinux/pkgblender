package com.thestaticvoid.blender.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "packages")
public class Package {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@OneToMany(mappedBy="pkg", cascade = CascadeType.ALL)
	private Set<Branch> branches = new HashSet<Branch>();
	
	@ManyToOne
	@JoinColumn(name = "maintainer_id", nullable = false)
	private User maintainer;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "validation_id")
	private Validation validation;

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getMaintainer() {
		return maintainer;
	}

	public void setMaintainer(User maintainer) {
		this.maintainer = maintainer;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}
		
	public Set<Branch> getBranches() {
		return branches;
	}
	
	public void addBranch(Branch branch) {
		branches.add(branch);
	}
}
