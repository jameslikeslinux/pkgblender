package com.thestaticvoid.blender.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "branches")
public class Branch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "pkg_id", nullable = false)
	private Package pkg;
	
	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
	private Set<Build> builds;
	
	@ElementCollection
	@CollectionTable(name = "branch_dependencies", joinColumns = @JoinColumn(name = "branch_id", nullable = false))
	@Column(name = "dependency")
	private Set<String> dependencies;
	
	@ManyToMany
	@JoinTable(name = "branch_oses", joinColumns = @JoinColumn(name = "branch_id"), inverseJoinColumns = @JoinColumn(name = "os_id"))
	private Set<Os> oses;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Package getPkg() {
		return pkg;
	}

	public void setPkg(Package pkg) {
		this.pkg = pkg;
	}

	public Set<Build> getBuilds() {
		return builds;
	}

	public void setBuilds(Set<Build> builds) {
		this.builds = builds;
	}
	
	public void addBuild(Build build) {
		builds.add(build);
	}

	public Set<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Set<String> dependencies) {
		this.dependencies = dependencies;
	}

	public Set<Os> getOses() {
		return oses;
	}

	public void setOses(Set<Os> oses) {
		this.oses = oses;
	}
}