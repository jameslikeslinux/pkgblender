package com.thestaticvoid.blender.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	ROLE_USER, ROLE_APPROVER, ROLE_ADMIN;

	public String getAuthority() {
		return name();
	}
}
