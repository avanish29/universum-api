package com.universum.service.label.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.service.label.domin.ResourceMessage;

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
	private Long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime created;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime lastUpdate;
	private String resourceName;
	private String resourceValue;
	
	public ResourceMessageDTO(final String resourceKey, final String resourceValue) {
		this.resourceName = resourceKey;
		this.resourceValue = resourceValue;
	}
	
	public static ResourceMessageDTO fromEntity(final ResourceMessage entity) {
        return ResourceMessageDTO.builder()
                .id(entity.getId())
                .created(entity.getCreated())
                .lastUpdate(entity.getLastUpdate())
                .resourceName(entity.getResourceName())
                .resourceValue(entity.getResourceValue())
                .build();
    }
}
