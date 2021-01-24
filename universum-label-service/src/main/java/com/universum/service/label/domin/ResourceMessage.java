package com.universum.service.label.domin;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.universum.common.jpa.domin.AbstractBaseEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the resource_message database table.
 */
@Table(name = "resource_message")
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ResourceMessage extends AbstractBaseEntity {
	private static final long serialVersionUID = 4647833846123040755L;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "available_language_fk")
	private AvailableLanguage availableLanguageFk;
	
	@Column(name = "resource_name")
	private String resourceName;
	
	@Column(name = "resource_value")
	private String resourceValue;
	
	public ResourceMessage(final String resourceName, final String resourceValue) {
		this.resourceName = resourceName;
		this.resourceValue = resourceValue;
	}
}