package com.thestaticvoid.blender.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "validations")
public class Validation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "validator_id", nullable = false)
	private User validator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validation_date", nullable = false, updatable = false)
	private Date validationDate = new Date();

	public User getValidator() {
		return validator;
	}

	public void setValidator(User validator) {
		this.validator = validator;
	}
}
