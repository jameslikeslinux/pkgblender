package com.thestaticvoid.blender.domain;

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
import javax.persistence.Table;

@Entity
@Table(name = "versions")
public class Version {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "package_id", nullable = false)
	private Package pkg;
	
	@Column(name = "version", nullable = false)
	private String version;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="version")
	private Set<Build> builds;
}
