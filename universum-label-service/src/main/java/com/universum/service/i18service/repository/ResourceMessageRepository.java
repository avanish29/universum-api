package com.universum.service.i18service.repository;

import java.util.List;

import com.universum.service.i18service.entity.ResourceMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface ResourceMessageRepository extends CrudRepository<ResourceMessage, Long> {
	
	@Query("SELECT new ResourceMessage(rm.resourceName, rm.resourceValue) FROM ResourceMessage rm LEFT JOIN AvailableLanguage al ON rm.availableLanguageFk = al.id WHERE al.code = :langCode")
	public List<ResourceMessage> findByLangCode(@Param(value="langCode") final String langCode);
}
