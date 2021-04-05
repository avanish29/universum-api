package com.universum.service.label.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.universum.service.label.domin.AvailableLanguage;

@Repository
public interface AvailableLanguageRepository extends JpaRepository<AvailableLanguage, Long>{
	
	public List<AvailableLanguage> findAllByDeletedFalse();
	
	public Optional<AvailableLanguage> findByCode(final String code);
	
	public long countByCode(final String code);
	
	@Query(nativeQuery=true, value="SELECT rm.key, rm.value FROM available_language_messages rm LEFT JOIN available_language al ON rm.available_language_id = al.id WHERE al.code = :langCode and al.deleted = false")
	public List<String[]> findAllMessagesByLangCode(@Param(value="langCode") final String langCode);
	
	@Query(nativeQuery=true, value="UPDATE available_language_messages SET key = '' WHERE rm.available_language_id = (select id from available_language al WHERE al.code = :langCode and al.deleted = false) and key = :messageCode")
	@Modifying
	public void deleteMessagesByLangCode(@Param(value="langCode") final String langCode, @Param(value="messageCode") final String messageCode);
	
	default Map<String, String> findAllMessagesByLangCodeAsMap(final String langCode) {
        return findAllMessagesByLangCode(langCode).stream().collect(Collectors.toMap(message -> message[0], message -> message[1]));
    }
}
