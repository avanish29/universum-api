package com.universum.service.label.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universum.service.label.domin.AvailableLanguage;

@Repository
public interface AvailableLanguageRepository extends JpaRepository<AvailableLanguage, Long>{
	public Page<AvailableLanguage> findAllByDeletedFalse(final Pageable pageable);
	
	public AvailableLanguage findByCode(final String code);
}
