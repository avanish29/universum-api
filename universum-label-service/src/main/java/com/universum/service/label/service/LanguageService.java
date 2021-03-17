package com.universum.service.label.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universum.common.exception.NotFoundException;
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
    	AvailableLanguage languageEntity = languageRepository.findByCode(langCode);
    	if(languageEntity == null || languageEntity.getDeleted()) {
    		throw new NotFoundException(String.format("Language with code '%s' does not exists!", langCode));
    	}
        return LanguageView.fromEntity(languageEntity);
    }
	
	@Caching(put = {@CachePut(value = LANGUAGE_CACHE_NAME, key = "#createRequest.code")},
			evict = {@CacheEvict(value = ALL_LANGUAGE_CACHE_NAME, allEntries = true)})
    public LanguageView addLanguage(@NotNull final CreateLanguageRequest createRequest){
    	log.debug("Adding new language with code {}.", createRequest.getCode());
    	AvailableLanguage languageEntity = languageRepository.findByCode(createRequest.getCode());
    	if(languageEntity != null && !languageEntity.getDeleted()) {
    		throw new ValidationException(String.format("Language with code '%s' already exists!", createRequest.getCode()));
    	}
    	AvailableLanguage newLanguageEntity = languageRepository.save(convertToEntity(createRequest));
        return LanguageView.fromEntity(newLanguageEntity);
    }
	
	@Caching(evict = {
			@CacheEvict(value = ALL_LANGUAGE_CACHE_NAME, allEntries = true),
			@CacheEvict(value = LANGUAGE_CACHE_NAME, key = "#p0")})
    public LanguageView updateLanguage(@NotNull final String langCode, @NotNull final LanguageRequest updateRequest){
    	log.debug("Updating language with code {}.", langCode);
    	AvailableLanguage languageEntity = languageRepository.findByCode(langCode);
    	if(languageEntity == null || languageEntity.getDeleted()) {
    		throw new NotFoundException(String.format("Language with code '%s' does not exists!", langCode));
    	}
    	AvailableLanguage updatedLanguageEntity = languageRepository.save(convertToEntity(updateRequest));
    	return LanguageView.fromEntity(updatedLanguageEntity);
    }
	
	@Caching(evict = {
			@CacheEvict(value = ALL_LANGUAGE_CACHE_NAME, allEntries = true),
			@CacheEvict(value = LANGUAGE_CACHE_NAME, key = "#p0")})
    public void deleteLanguage(@NotNull final String langCode){
    	log.debug("Deleting language with code {}.", langCode);
    	AvailableLanguage languageEntity = languageRepository.findByCode(langCode);
    	if(languageEntity == null || languageEntity.getDeleted()) {
    		throw new NotFoundException(String.format("Language with code '%s' does not exists!", langCode));
    	}
    	languageRepository.deleteMessagesByLangCode(langCode);
    	languageRepository.delete(languageEntity);
    }
    
	@Cacheable(value = LANGUAGE_MESSAGE_CACHE_NAME, unless = "#result == null")
    public Map<String, String> findMessagesByLangCode(final String langCode){
		log.debug("Finding all messages for language code {}.", langCode);
		return languageRepository.findAllMessagesByLangCodeAsMap(langCode);
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
