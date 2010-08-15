package com.thestaticvoid.blender.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "builds")
public class Build {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;
	
	@Column(name = "version")
	private String version;
	
	@Column(name = "build_num")
	private int buildNum;
	
	@Column(name = "svn_rev")
	private int svnRevision;
	
	@ManyToMany(mappedBy = "builds")
	private Set<Repository> repositories;
}
