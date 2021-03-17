package com.universum.service.label.domin;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the resource_message database table.
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
	private static final long serialVersionUID = 3249940058950663424L;
	
	@NotNull
	private String key;
	
	@NotNull
	private String value;
}