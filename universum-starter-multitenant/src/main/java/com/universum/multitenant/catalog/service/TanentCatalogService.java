package com.universum.multitenant.catalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universum.common.exception.NotFoundException;
import com.universum.multitenant.catalog.annotation.CatalogTransactional;
import com.universum.multitenant.catalog.domain.TanentCatalog;
import com.universum.multitenant.catalog.repository.TanentCatalogRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CatalogTransactional
public class TanentCatalogService {
	@Autowired
	private TanentCatalogRepository catalogRepository;
	
	@CatalogTransactional(readOnly = true)
	public List<TanentCatalog> findAll() {
		log.info("Finding all tenants.");
		return catalogRepository.findAll();
	}
	
	@CatalogTransactional(readOnly = true)
	public TanentCatalog findByTenant(final String tenantName) {
		log.info("Finding tenant by name {}.", tenantName);
		return catalogRepository.findByTenantNameIgnoreCase(tenantName).orElseThrow(() -> new NotFoundException(TanentCatalog.class, tenantName));
	}
}
