package com.universum.service.i18service.controller;

import java.util.Map;

import com.universum.service.i18service.entity.ResourceMessage;
import com.universum.service.i18service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@GetMapping
    public Iterable<ResourceMessage> getAllMessages(){
        return messageService.getAllMessages();
    }
	
	@GetMapping("/{langCode}")
    public Map<String, String> getMessagesByLangCode(@PathVariable String langCode){
		return messageService.findMessagesByLangCode(langCode);
    }
}
