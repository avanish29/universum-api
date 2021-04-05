package com.universum.service.label.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universum.common.exception.NotFoundException;
import com.universum.security.util.AuthenticationConstant;
import com.universum.service.label.domin.AvailableLanguage;
import com.universum.service.label.dto.CreateLanguageRequest;
import com.universum.service.label.dto.LanguageRequest;
import com.universum.service.label.dto.LanguageView;
import com.universum.service.label.repository.AvailableLanguageRepository;
import com.universum.service.label.util.LanguageDirection;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class LanguageService {
	private static final String ALL_LANGUAGE_CACHE_NAME = "LANGUAGES_CACHE";
	private static final String LANGUAGE_CACHE_NAME = "LANGUAGE_CACHE";
	private static final String LANGUAGE_MESSAGE_CACHE_NAME = "LANGUAGE_MESSAGE_CACHE";
	
	@Autowired
	private AvailableLanguageRepository languageRepository;
	
	@Cacheable(ALL_LANGUAGE_CACHE_NAME)
	public List<LanguageView> getAllLanguages(){
		log.debug("Getting getAllLanguages()");
		List<AvailableLanguage> allLanguages = languageRepository.findAllByDeletedFalse();
		return Optional.of(allLanguages).orElseGet(Collections::emptyList).stream().map(LanguageView::fromEntity).collect(Collectors.toList());
    }

	@Cacheable(value = LANGUAGE_CACHE_NAME, unless = "#result == null")
    public LanguageView findByCode(@NotNull final String langCode){
    	log.debug("Calling findByCode() for language code {}.", langCode);
        return LanguageView.fromEntity(Optional.of(this.findOneByCodeOrNotFound(langCode)));
    }
	
	@Caching(put = {@CachePut(value = LANGUAGE_CACHE_NAME, key = "#createRequest.code")},
			evict = {@CacheEvict(value = ALL_LANGUAGE_CACHE_NAME, allEntries = true)
	})
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
    public LanguageView addLanguage(@NotNull final CreateLanguageRequest createRequest){
    	log.debug("Adding new language with code {}.", createRequest.getCode());
    	
    	Optional.of(createRequest.getCode())
				.map(languageRepository::countByCode)
				.filter(count -> count >= 1)
				.ifPresent(
					count -> {
						throw new ValidationException(String.format("Language with code '%s' already exists!", createRequest.getCode()));
					}
				);
    	final AvailableLanguage newLanguageEntity = languageRepository.save(convertToEntity(createRequest));
        return LanguageView.fromEntity(Optional.of(newLanguageEntity));
    }
	
	@Caching(evict = {
			@CacheEvict(value = ALL_LANGUAGE_CACHE_NAME, allEntries = true),
			@CacheEvict(value = LANGUAGE_CACHE_NAME, key = "#p0")
	})
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
    public LanguageView updateLanguage(@NotNull final String langCode, @NotNull final LanguageRequest updateRequest){
    	log.debug("Updating language with code {}.", langCode);
    	
    	Optional<AvailableLanguage> updatedLanguageEntity = Optional.of(this.findOneByCodeOrNotFound(langCode))
    			.map(languageEntity -> {
		    		languageEntity.setDir(LanguageDirection.valueOf(updateRequest.getDir()));
		        	languageEntity.setIsDefault(updateRequest.getIsDefault());
		        	languageEntity.setLabel(updateRequest.getLabel());
		        	languageEntity = languageRepository.save(languageEntity);
		        	log.debug("Changed Information for Language: {}", languageEntity);
		    		return languageEntity;
		    	});
    	return LanguageView.fromEntity(updatedLanguageEntity);
    }
	
	@Caching(evict = {
			@CacheEvict(value = ALL_LANGUAGE_CACHE_NAME, allEntries = true),
			@CacheEvict(value = LANGUAGE_CACHE_NAME, key = "#p0")
	})
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
    public LanguageView deleteLanguage(@NotNull final String langCode){
    	log.debug("Deleting language with code {}.", langCode);
    	
    	Optional<AvailableLanguage> deletedLanguage = Optional.of(this.findOneByCodeOrNotFound(langCode))
				.map(languageEntity -> {
					languageEntity.setCode(String.format("_%s_%s", String.valueOf(languageEntity.getId()), languageEntity.getCode()));
					languageEntity.setDeleted(Boolean.TRUE);
					languageEntity = languageRepository.save(languageEntity);
					log.debug("Deleted Language: {}", languageEntity);
					return languageEntity;
				});
    	return LanguageView.fromEntity(deletedLanguage);
    }
    
	@Cacheable(value = LANGUAGE_MESSAGE_CACHE_NAME, unless = "#result == null")
    public Map<String, String> findMessagesByLangCode(final String langCode){
		log.debug("Finding all messages for language code {}.", langCode);
		return languageRepository.findAllMessagesByLangCodeAsMap(langCode);
    }
	
	@CacheEvict(value = LANGUAGE_MESSAGE_CACHE_NAME)
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public void deleteMessage(@NotNull final String langCode, @NotNull final String messageKey) {
		log.debug("Deleting resource mesasge with key {} for language code {}.", messageKey, langCode);
		this.languageRepository.deleteMessagesByLangCode(langCode, messageKey);
	}
	
	@CacheEvict(value = LANGUAGE_MESSAGE_CACHE_NAME)
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public void updateMessage(@NotNull final String langCode, @NotNull final String messageKey) {
		log.debug("Updating resource mesasge with key {} for language code {}.", messageKey, langCode);
		this.languageRepository.deleteMessagesByLangCode(langCode, messageKey);
	}
	
	AvailableLanguage findOneByCodeOrNotFound(@NotNull final String langCode) {
		Optional<AvailableLanguage> languageByCode = languageRepository.findByCode(langCode);
		if(languageByCode.isEmpty() || BooleanUtils.isTrue(languageByCode.get().getDeleted())) {
			throw new NotFoundException(AvailableLanguage.class, langCode);
		}
		return languageByCode.get();
	}
    
    protected AvailableLanguage convertToEntity(@NotNull final LanguageRequest languageRequest) {
    	AvailableLanguage languageEntity = new AvailableLanguage();
    	if(languageRequest.getClass().isAssignableFrom(CreateLanguageRequest.class)) {
    		languageEntity.setCode(((CreateLanguageRequest)languageRequest).getCode());
    	}
    	languageEntity.setDir(LanguageDirection.valueOf(languageRequest.getDir()));
    	languageEntity.setIsDefault(languageRequest.getIsDefault());
    	languageEntity.setLabel(languageRequest.getLabel());
    	return languageEntity;
    }
    
}
