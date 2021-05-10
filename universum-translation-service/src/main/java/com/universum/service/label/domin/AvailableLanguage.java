package com.universum.service.label.domin;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.universum.common.domain.AuditingBaseModel;
import com.universum.service.label.util.LanguageDirection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the available_language database table.
 * 
 */
@Table(name = "available_language")
@Entity
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "deleted is NULL or deleted != true")
public class AvailableLanguage extends AuditingBaseModel {
	private static final long serialVersionUID = 510614895831100389L;
	
	
	@Id
    @Column(unique = true)
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected Long id;
	
	@Column(unique=true, nullable=false, length = 36)
	private String code;
	
	@Enumerated(EnumType.STRING)
	private LanguageDirection dir = LanguageDirection.defaultDirection();
	
	@Column(name = "is_default")
	private Boolean isDefault = Boolean.FALSE;
	
	private String label;
	
	@Embedded
	@Basic(fetch=FetchType.LAZY)
	private LanguageMessage messages;
}