package com.universum.service.label.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.service.label.domin.AvailableLanguage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LanguageView implements Serializable {
	private static final long serialVersionUID = -5237840804827593460L;
	private Long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime createdOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime updatedOn;
	private String updatedBy;
	private String code;
	private String dir;
	private Boolean isDefault;
	private String label;
	
	public static LanguageView fromEntity(final Optional<AvailableLanguage> entity) {
		if(!entity.isPresent()) return null;
		AvailableLanguage languageEntity = entity.get();
        return LanguageView.builder()
                .id(languageEntity.getId())
                .createdOn(languageEntity.getCreatedOn())
                .createdBy(languageEntity.getCreatedBy())
                .updatedOn(languageEntity.getUpdatedOn())
                .updatedBy(languageEntity.getUpdatedBy())
                .code(languageEntity.getCode())
                .dir(languageEntity.getDir().toString())
                .isDefault(languageEntity.getIsDefault())
                .label(languageEntity.getLabel())
                .build();
    }
	
	public static LanguageView fromEntity(final AvailableLanguage entity) {
		return fromEntity(Optional.of(entity));
	}
}
