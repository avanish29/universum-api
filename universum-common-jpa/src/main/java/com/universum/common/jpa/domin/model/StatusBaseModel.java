package com.universum.common.jpa.domin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class StatusBaseModel extends AuditingBaseModel {
	private static final long serialVersionUID = 4296063824746938905L;
	private int status;
}
