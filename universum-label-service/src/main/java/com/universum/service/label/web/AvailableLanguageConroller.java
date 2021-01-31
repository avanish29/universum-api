package com.universum.service.label.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.label.dto.AvailableLanguageDTO;
import com.universum.service.label.service.LanguageService;

@RestController
@RequestMapping("/languages")
public class AvailableLanguageConroller {
	@Autowired
	private LanguageService languageService;
	
	@GetMapping
    public UniversumPageResponse<AvailableLanguageDTO> getAllLanguages(@Valid UniversumPageRequest pageRequest){
        return languageService.getAllLanguages(pageRequest);
    }
	
	@GetMapping("/{langCode}")
    public AvailableLanguageDTO getLanguagesByCode(@PathVariable String langCode){
		return languageService.findByCode(langCode);
    }
}
