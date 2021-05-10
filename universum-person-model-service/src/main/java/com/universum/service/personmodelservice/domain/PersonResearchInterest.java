package com.universum.service.personmodelservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;

import com.universum.common.enums.Translation;
import com.universum.common.domain.cerif.CerifMultipleLingualModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="cfpersresint", uniqueConstraints=@UniqueConstraint(columnNames={"cf_persid","cf_langcode","cf_trans"}))
@Where(clause = "deleted is NULL or deleted != true")
@SequenceGenerator(name = PersonResearchInterest.PERSON_RESEARCH_INTEREST_SEQUENCE_GENERATOR_NAME, sequenceName = PersonResearchInterest.PERSON_RESEARCH_INTEREST_SEQUENCE_GENERATOR_NAME, allocationSize = 1)
public class PersonResearchInterest extends CerifMultipleLingualModel {
	private static final long serialVersionUID = -2791975196729927438L;

	public static final String PERSON_RESEARCH_INTEREST_SEQUENCE_GENERATOR_NAME = "cfperskeyw_sequence";
	
	/**
	 * Primary Key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERSON_RESEARCH_INTEREST_SEQUENCE_GENERATOR_NAME)
	private Long id;
	
	/**
	 * The person.
	 */
	@ManyToOne(optional=false)
	@JoinColumn(name="cf_persid")
	private Person person;
	
	/**
	 * The language.
	 */
	@Column(name="cf_langcode")
	private String language;
	
	/**
	 * The translation.
	 */
	@NotNull
	@Column(name="cf_trans")
	@Enumerated(EnumType.STRING)
	private Translation translation;
	
	/**
	 * The person's research interests.
	 */
	@NotNull
	@Column(name="cf_resint", length=20000)
	private String researchInterests;
}
