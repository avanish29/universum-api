package com.universum.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universum.security.entity.ApplicationRole;

@Repository
public interface AppRoleRepository extends JpaRepository<ApplicationRole, Long> {
	public List<ApplicationRole> findAllByDeletedFalse();
}
