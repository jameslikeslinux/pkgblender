package com.thestaticvoid.blender.service;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class OsDetails {
	@NotEmpty
	private String name;
	
	@NotEmpty
	@Pattern(regexp = "^\\w*$", message = "invalid.slug")
	private String slug;
	
	@NotEmpty
	@URL(message = "invalid.url")
	private String publisher;
	
	@NotEmpty
	private String branch;
	
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
}
