package com.universum.multitenant.catalog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universum.multitenant.catalog.domain.TanentCatalog;

@Repository
public interface TanentCatalogRepository extends JpaRepository<TanentCatalog, Long> {
	Optional<TanentCatalog> findByTenantNameIgnoreCase(final String tenantName);
}
