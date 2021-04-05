package com.universum.service.security.repository;

import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.universum.common.exception.NotFoundException;
import com.universum.service.security.entity.ApplicationRole;

@Repository
public interface AppRoleRepository extends JpaRepository<ApplicationRole, Long> {
	public Page<ApplicationRole> findAllByDeletedFalse(final Pageable pageable);
	
	public int countByNameAndDeletedFalseIgnoreCase(final String name);
	
	@Query(nativeQuery = true, value = "SELECT count(user_id) from app_user_roles where role_id= :roleId")
	public int countUserByRoleId(@Param("roleId") final Long roleId);
	
	public Optional<ApplicationRole> findOneByNameAndDeletedFalse(final String name);
	
	public default ApplicationRole findByIdNotDeleted(final Long id) {
		Optional<ApplicationRole> optionalRole = findById(id);
		if(optionalRole.isEmpty() || BooleanUtils.isTrue(optionalRole.get().getDeleted())) {
			throw new NotFoundException(ApplicationRole.class, id);
		}
		return optionalRole.get();
	}
}
