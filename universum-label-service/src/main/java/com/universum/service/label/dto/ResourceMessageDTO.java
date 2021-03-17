package com.universum.service.label.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.service.label.domin.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMessageDTO implements Serializable {
	private static final long serialVersionUID = -5237840804827593460L;
	private String id;
	private String resourceKey;
	private String resourceValue;
	
	public ResourceMessageDTO(final String resourceKey, final String resourceValue) {
		this.resourceKey = resourceKey;
		this.resourceValue = resourceValue;
	}
	
	public static ResourceMessageDTO fromEntity(final Message entity) {
        return ResourceMessageDTO.builder()
                .resourceKey(entity.getKey())
                .resourceValue(entity.getValue())
                .build();
    }
}
