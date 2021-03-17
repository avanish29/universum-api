package com.universum.service.label.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.universum.service.label.dto.CreateLanguageRequest;
import com.universum.service.label.dto.LanguageRequest;
import com.universum.service.label.dto.LanguageView;
import com.universum.service.label.service.LanguageService;

@RestController
@RequestMapping("/languages")
public class AvailableLanguageConroller {
	@Autowired
	private LanguageService languageService;
	
	@GetMapping
    public List<LanguageView> getAllLanguages(){
        return languageService.getAllLanguages();
    }
	
	@PostMapping()
	public ResponseEntity<LanguageView> createRole(@RequestBody @Valid CreateLanguageRequest createRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(languageService.addLanguage(createRequest));
	}
	
	@GetMapping("{langCode}")
    public LanguageView getLanguagesByCode(@PathVariable String langCode){
		return languageService.findByCode(langCode);
    }
	
	@PutMapping("{langCode}")
    public LanguageView updateLanguage(@PathVariable String langCode, @RequestBody @Valid LanguageRequest updateRequest){
		return languageService.updateLanguage(langCode, updateRequest);
    }
	
	@DeleteMapping("{langCode}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable String langCode) {
		languageService.deleteLanguage(langCode);
	}
	
	@GetMapping("/{langCode}/messages")
    public Map<String, String> getMessagesByLangCode(@PathVariable String langCode){
		return languageService.findMessagesByLangCode(langCode);
    }
}
