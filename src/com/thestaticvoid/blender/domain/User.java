package com.thestaticvoid.blender.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {
	private static final long serialVersionUID = 4451597884273035934L;

	@Id
	@GeneratedValue
	private int id;

	@Column(length = 8, nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@OneToOne(mappedBy = "user")
	private EmailValidationToken emailValidationToken;
	
	private boolean enabled = true;
	
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name = "user_id", nullable = false))
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Set<Role> roles;
	
	@Column(name = "creation_date", updatable = false)
	private Date creationDate = new Date();
	
	@Column(name = "last_login")
	private Date lastLogin;
	
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public EmailValidationToken getEmailValidationToken() {
		return emailValidationToken;
	}
	
	public void setEmailValidationToken(EmailValidationToken emailValidationToken) {
		this.emailValidationToken = emailValidationToken;
	}
	
	public boolean isEmailValid() {
		return emailValidationToken == null;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		if (!isEmailValid())
			return new HashSet<GrantedAuthority>();			
			
		return new HashSet<GrantedAuthority>(getRoles());
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}
}
