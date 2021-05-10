package com.universum.service.personmodelservice.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

import com.universum.common.enums.Gender;
import com.universum.common.domain.cerif.CerifModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cfpers")
@Where(clause = "deleted is NULL or deleted != true")
@SequenceGenerator(name = Person.PERSON_SEQUENCE_GENERATOR_NAME, sequenceName = Person.PERSON_SEQUENCE_GENERATOR_NAME, allocationSize = 1)
public class Person extends CerifModel {
	private static final long serialVersionUID = 4746500986059497527L;
	public static final String PERSON_SEQUENCE_GENERATOR_NAME = "cfpers_sequence";
	
	@Id
    @Column(name = "cf_persid", unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERSON_SEQUENCE_GENERATOR_NAME)
    protected Long id;
	
	@Column(name="cf_birthdate")
	private LocalDate birthDate;
	
	@Column(name="cf_gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name="cf_uri")
	private String uri;
	
	@Column(name="cf_uuid")
	private String uuid;
	
	/**
	 * Multilingual.
	 */
	@OneToMany(mappedBy="person")
	private Set<PersonKeyword> personKeywords;
	
	@OneToMany(mappedBy="person")
	private Set<PersonResearchInterest> personResearchInterests;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Person person = (Person) o;

		return id != null && id.equals(person.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this);
	}
}
