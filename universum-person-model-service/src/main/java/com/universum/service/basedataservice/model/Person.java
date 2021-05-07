package com.universum.service.basedataservice.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.universum.common.jpa.domin.CerifEntity;
import com.universum.common.jpa.enumrations.Gender;

@Entity
@Table(name="cfPers")
public class Person extends CerifEntity {
	private static final long serialVersionUID = 4746500986059497527L;
	
	@Column(name="cfBirthdate")
	private LocalDate birthDate;
	
	@Column(name="cfGender")
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name="cfURI")
	private String uri;
	
	@Column(name="cfUUID")
	private String uuid;
}
