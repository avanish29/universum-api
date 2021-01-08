package com.universum.security.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.universum.security.entity.ApplicationRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse implements Serializable {
	private static final long serialVersionUID = -7597263576559928902L;
	private Long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private LocalDateTime created;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private LocalDateTime lastUpdate;
	private String name;
    private String description;
    private Boolean isSystem;
    
    public static RoleResponse fromEntity(final ApplicationRole entity) {
        return RoleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .created(entity.getCreated())
                .lastUpdate(entity.getLastUpdate())
                .isSystem(entity.getIsSystem())
                .build();
    }
}
