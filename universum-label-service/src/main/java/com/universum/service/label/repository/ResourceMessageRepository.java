package com.universum.service.label.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.universum.service.label.domin.ResourceMessage;


public interface ResourceMessageRepository extends CrudRepository<ResourceMessage, Long> {
	public Page<ResourceMessage> findAllByDeletedFalse(final Pageable pageable);
	
	@Query("SELECT new com.universum.service.label.domin.ResourceMessage(rm.resourceName, rm.resourceValue) FROM ResourceMessage rm LEFT JOIN AvailableLanguage al ON rm.availableLanguageFk = al.id WHERE al.code = :langCode")
	public List<ResourceMessage> findByLangCode(@Param(value="langCode") final String langCode);
	
	default Map<String, String> findByLangCodeAsMap(final String langCode) {
        return findByLangCode(langCode).stream().collect(Collectors.toMap(ResourceMessage::getResourceName, ResourceMessage::getResourceValue));
    }

}
