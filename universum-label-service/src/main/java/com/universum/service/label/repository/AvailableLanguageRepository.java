package com.universum.service.label.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.universum.service.label.domin.AvailableLanguage;

@Repository
public interface AvailableLanguageRepository extends CrudRepository<AvailableLanguage, Long>{
	public AvailableLanguage findByCode(final String code);
}
