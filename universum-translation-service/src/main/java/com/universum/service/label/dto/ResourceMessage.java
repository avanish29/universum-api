package com.universum.service.label.dto;

import java.io.Serializable;

import com.universum.service.label.domin.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMessage implements Serializable {
	private static final long serialVersionUID = -5237840804827593460L;
	private String resourceKey;
	private String resourceValue;
	
	public static ResourceMessage fromEntity(final Message entity) {
        return ResourceMessage.builder()
                .resourceKey(entity.getKey())
                .resourceValue(entity.getValue())
                .build();
    }
}
