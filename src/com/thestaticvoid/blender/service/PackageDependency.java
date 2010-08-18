package com.thestaticvoid.blender.service;

public class PackageDependency implements Comparable<PackageDependency> {
	public enum Status {
		OK, MISSING
	}
	
	private String name;
	private Status status;
	private String realName;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getRealName() {
		return realName;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public int compareTo(PackageDependency o) {
		return name.compareTo(o.name);
	}
}
