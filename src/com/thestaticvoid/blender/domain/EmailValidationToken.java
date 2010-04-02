package com.thestaticvoid.blender.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "email_validation_tokens")
public class EmailValidationToken {
	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne
	@JoinColumn(name = "user_id", unique = true, nullable = false, updatable = false)
	private User user;
	
	@Column(nullable = false)
	private String token;
	
	@Column(name = "modification_date")
	private Date modificationDate;
	
	public int getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public Date getModificationDate() {
		return modificationDate;
	}
	
	@SuppressWarnings("unused")
	@PrePersist
	@PreUpdate
	private void setModificationDate() {
		modificationDate = new Date();
	}
}