package com.universum.service.label.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import com.universum.service.label.domin.ResourceMessage;
import com.universum.service.label.dto.ResourceMessageDTO;
import com.universum.service.label.repository.ResourceMessageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class MessageService {
	@Autowired
	private ResourceMessageRepository messageRepository;
	
	public UniversumPageResponse<ResourceMessageDTO> getAllMessages(final UniversumPageRequest appPageRequest){
		log.debug("Loading all messages");
		Optional<Direction> sortDir = Direction.fromOptionalString(appPageRequest.getOrder());
		PageRequest pageRequest = PageRequest.of(appPageRequest.getOffset(), appPageRequest.getLimit(), sortDir.isPresent() ? sortDir.get() : Direction.ASC , appPageRequest.getSort());
		
		Page<ResourceMessage> pageResponse = messageRepository.findAllByDeletedFalse(pageRequest);
		List<ResourceMessageDTO> mesasgeDTOList = Optional.of(pageResponse.getContent()).orElseGet(Collections::emptyList).stream()
                .map(ResourceMessageDTO::fromEntity).collect(Collectors.toList());
        
        return new UniversumPageResponse<>(pageResponse.getTotalElements(), pageResponse.getTotalPages(), pageResponse.getNumber(), mesasgeDTOList);
    }
	
	public Map<String, String> findMessagesByLangCode(final String langCode){
		log.debug("Finding all messages for language code {}.", langCode);
		return messageRepository.findByLangCodeAsMap(langCode);
    }
}
