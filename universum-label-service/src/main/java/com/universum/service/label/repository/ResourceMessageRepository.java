package com.universum.service.label.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.universum.service.label.domin.ResourceMessage;


public interface ResourceMessageRepository extends CrudRepository<ResourceMessage, Long> {
	
	@Query("SELECT new ResourceMessage(rm.resourceName, rm.resourceValue) FROM ResourceMessage rm LEFT JOIN AvailableLanguage al ON rm.availableLanguageFk = al.id WHERE al.code = :langCode")
	public List<ResourceMessage> findByLangCode(@Param(value="langCode") final String langCode);
}
