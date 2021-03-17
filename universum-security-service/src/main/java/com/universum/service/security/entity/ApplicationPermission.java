package com.universum.service.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

import com.universum.common.jpa.domin.AbstractBaseEntity;
import com.universum.service.security.util.UserAction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
@Table(name = "app_permission", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "action" }) })
public class ApplicationPermission extends AbstractBaseEntity {
	private static final long serialVersionUID = 1487227621155711682L;
	@Column(nullable = false)
    private String name;
    private String description;
    @Enumerated(EnumType.ORDINAL)
    private UserAction action;
    private String route;
    private Boolean isClientSpecific;
}
