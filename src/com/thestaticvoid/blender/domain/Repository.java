package com.thestaticvoid.blender.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "repositories")
public class Repository {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@ManyToMany
	@JoinTable(name = "repository_builds",
		joinColumns = @JoinColumn(name = "repository_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "build_id", referencedColumnName = "id"))
	private Set<Build> builds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
}
