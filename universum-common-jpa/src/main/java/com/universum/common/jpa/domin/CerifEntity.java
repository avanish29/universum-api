package com.universum.common.jpa.domin;

import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@Audited
public class CerifEntity extends AbstractBaseEntity {
	private static final long serialVersionUID = -5501899274079754133L;
	private int status;
}
