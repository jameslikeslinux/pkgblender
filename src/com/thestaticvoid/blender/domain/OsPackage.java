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
}
