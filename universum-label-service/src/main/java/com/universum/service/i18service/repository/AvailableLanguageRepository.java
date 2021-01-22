package com.universum.service.i18service.repository;

import com.universum.service.i18service.entity.AvailableLanguage;
import org.springframework.data.repository.CrudRepository;


public interface AvailableLanguageRepository extends CrudRepository<AvailableLanguage, Long>{
	public AvailableLanguage findByCode(final String code);
}
