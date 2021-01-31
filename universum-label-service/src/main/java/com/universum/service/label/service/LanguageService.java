package com.universum.service.label.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.label.domin.AvailableLanguage;
import com.universum.service.label.dto.AvailableLanguageDTO;
import com.universum.service.label.repository.AvailableLanguageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class LanguageService {
	@Autowired
	private AvailableLanguageRepository languageRepository;
	
	public UniversumPageResponse<AvailableLanguageDTO> getAllLanguages(final UniversumPageRequest appPageRequest){
		log.debug("Calling getAllLanguages()");
		Optional<Direction> sortDir = Direction.fromOptionalString(appPageRequest.getOrder());
		PageRequest pageRequest = PageRequest.of(appPageRequest.getOffset(), appPageRequest.getLimit(), sortDir.isPresent() ? sortDir.get() : Direction.ASC , appPageRequest.getSort());
		
		Page<AvailableLanguage> pageResponse = languageRepository.findAllByDeletedFalse(pageRequest);
		List<AvailableLanguageDTO> roleDTOList = Optional.of(pageResponse.getContent()).orElseGet(Collections::emptyList).stream()
                .map(AvailableLanguageDTO::fromEntity).collect(Collectors.toList());
        
        return new UniversumPageResponse<>(pageResponse.getTotalElements(), pageResponse.getTotalPages(), pageResponse.getNumber(), roleDTOList);
    }

    public AvailableLanguageDTO findByCode(final String langCode){
    	log.debug("Calling findByCode() for language code {}.", langCode);
        return AvailableLanguageDTO.fromEntity(languageRepository.findByCode(langCode));
    } 
}
