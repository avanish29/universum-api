package com.universum.service.label.web;

import com.universum.service.label.domin.AvailableLanguage;
import com.universum.service.label.service.LanguageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/languages")
public class AvailableLanguageConroller {
	@Autowired
	private LanguageService languageService;
	
	@GetMapping
    public Iterable<AvailableLanguage> getAllLanguages(){
        return languageService.getAllLanguages();
    }
	
	@GetMapping("/{langCode}")
    public AvailableLanguage getLanguagesByCode(@PathVariable String langCode){
		return languageService.findByCode(langCode);
    }
}
