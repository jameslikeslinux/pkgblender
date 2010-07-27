package com.thestaticvoid.blender.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "email_validation_tokens")
public class EmailValidationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "user_id", unique = true, nullable = false, updatable = false)
	private User user;
	
	@Column(name = "token", nullable = false)
	private String token;
	
	@Temporal(TemporalType.TIMESTAMP)
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