package com.universum.service.personmodelservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.universum.common.domain.cerif.CerifAdditionalFeatureModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cfPersName")
public class PersonName extends CerifAdditionalFeatureModel {
	private static final long serialVersionUID = -5391090457373902000L;
	
	@Id
	@Column(name="cf_persnameid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * The person first names
	 */
	@Column(name="cf_firstnames")
	private String firstNames;
	
	/**
	 * The person family names
	 */
	@Column(name="cf_lastnames")
	private String lastName;
	
}
