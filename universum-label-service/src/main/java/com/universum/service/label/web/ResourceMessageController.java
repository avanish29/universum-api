package com.universum.service.label.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.service.label.dto.LanguageView;
import com.universum.service.label.service.LanguageService;

@RestController
@RequestMapping("/languages")
public class ResourceMessageController {
	@Autowired
	private LanguageService languageService;
	
	@GetMapping
    public List<LanguageView> getAllLanguages(){
        return languageService.getAllLanguages();
    }
	
	@GetMapping("/{langCode}/messages")
    public Map<String, String> getMessagesByLangCode(@PathVariable String langCode){
		return languageService.findMessagesByLangCode(langCode);
    }
}
