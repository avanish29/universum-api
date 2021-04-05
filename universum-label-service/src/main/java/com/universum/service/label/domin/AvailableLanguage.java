package com.universum.service.label.domin;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;

import com.universum.common.jpa.domin.AbstractBaseEntity;
import com.universum.service.label.util.LanguageDirection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the available_language database table.
 * 
 */
@Table(name = "available_language")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class AvailableLanguage extends AbstractBaseEntity {
	private static final long serialVersionUID = 510614895831100389L;
	
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