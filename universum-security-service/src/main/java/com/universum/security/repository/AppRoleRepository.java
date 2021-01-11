package com.universum.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universum.security.entity.ApplicationRole;

@Repository
public interface AppRoleRepository extends JpaRepository<ApplicationRole, Long> {
	public Page<ApplicationRole> findAllByDeletedFalse(final Pageable pageable);
}
