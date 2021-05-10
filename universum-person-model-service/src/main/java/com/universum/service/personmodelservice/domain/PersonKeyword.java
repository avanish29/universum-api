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
@Table(name = "cfperskeyw", uniqueConstraints = @UniqueConstraint(columnNames = {"cf_persid","cf_langcode","cf_trans"}))
@Where(clause = "deleted is NULL or deleted != true")
@SequenceGenerator(name = PersonKeyword.PERSON_KEYWORD_SEQUENCE_GENERATOR_NAME, sequenceName = PersonKeyword.PERSON_KEYWORD_SEQUENCE_GENERATOR_NAME, allocationSize = 1)
public class PersonKeyword extends CerifMultipleLingualModel {
	private static final long serialVersionUID = -7791574149165098575L;

	public static final String PERSON_KEYWORD_SEQUENCE_GENERATOR_NAME = "cfperskeyw_sequence";
	
	/**
	 * Primary Key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERSON_KEYWORD_SEQUENCE_GENERATOR_NAME)
	private Long id;
	
	/**
	 * The person.
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "cf_persid")
	private Person person;
	
	/**
	 * The translation.
	 */
	@Column(name = "cf_trans", nullable = false)
	@Enumerated(EnumType.STRING)
	private Translation translation;
	
	/**
	 * The person's keywords.
	 */
	@Column(name = "cf_keyw", length = 20000)
	private String keyword;
	
	/**
	 * The language.
	 */
	@Column(name = "cf_langcode")
	private String language;
}
