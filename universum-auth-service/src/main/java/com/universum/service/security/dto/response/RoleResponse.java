package com.universum.service.security.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.common.model.BaseResponse;
import com.universum.service.security.domain.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleResponse extends BaseResponse {
	private String name;
    private String description;
    private Boolean isSystem;
    
    public static RoleResponse fromEntity(final Role entity) {
        return builder()
                .id(entity.getId())
                .createdOn(entity.getCreatedOn())
                .createdBy(entity.getCreatedBy())
                .updatedOn(entity.getUpdatedOn())
                .updatedBy(entity.getUpdatedBy())
                .name(entity.getName())
                .description(entity.getDescription())
                .isSystem(entity.getIsSystem())
                .build();
    }
}
