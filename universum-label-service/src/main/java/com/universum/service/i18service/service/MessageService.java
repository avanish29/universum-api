package com.universum.service.i18service.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.universum.service.i18service.entity.ResourceMessage;
import com.universum.service.i18service.repository.ResourceMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class MessageService {
	@Autowired
	private ResourceMessageRepository messageRepository;
	
	public Iterable<ResourceMessage> getAllMessages(){
		log.debug("Loading all messages");
        return messageRepository.findAll();
    }
	
	public Map<String, String> findMessagesByLangCode(final String langCode){
		log.debug("Finding all messages for language code {}.", langCode);
		final List<ResourceMessage> messages = messageRepository.findByLangCode(langCode);
		final Map<String, String> resultMessages = messages.stream().collect(Collectors.toMap(ResourceMessage::getResourceName, ResourceMessage::getResourceValue));
		return resultMessages;
    }
}
