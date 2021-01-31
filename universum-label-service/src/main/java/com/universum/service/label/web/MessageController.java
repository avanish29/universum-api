package com.universum.service.label.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.label.dto.ResourceMessageDTO;
import com.universum.service.label.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@GetMapping
    public UniversumPageResponse<ResourceMessageDTO> getAllMessages(@Valid UniversumPageRequest pageRequest){
        return messageService.getAllMessages(pageRequest);
    }
	
	@GetMapping("/{langCode}")
    public Map<String, String> getMessagesByLangCode(@PathVariable String langCode){
		return messageService.findMessagesByLangCode(langCode);
    }
}
