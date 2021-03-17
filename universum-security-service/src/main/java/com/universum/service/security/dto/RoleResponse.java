package com.universum.service.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.common.model.BaseResponse;
import com.universum.service.security.entity.ApplicationRole;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleResponse extends BaseResponse {
	private static final long serialVersionUID = -5237840804827593460L;
	private String name;
    private String description;
    private Boolean isSystem;
    
    public static RoleResponse fromEntity(final ApplicationRole entity) {
        return builder()
                .id(entity.getId())
                .created(entity.getCreated())
                .lastUpdate(entity.getLastUpdate())
                .name(entity.getName())
                .description(entity.getDescription())
                .isSystem(entity.getIsSystem())
                .build();
    }
}
