package com.universum.service.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.universum.service.security.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	public Page<Role> findAll(final Pageable pageable);
	
	public int countByNameIgnoreCase(final String name);
	
	@Query(nativeQuery = true, value = "SELECT count(aur.user_id) from app_user_roles aur left join app_user au on aur.user_id = au.id where aur.role_id = :roleId and au.deleted = false")
	public int countUserByRoleId(@Param("roleId") final Long roleId);
	
	public Optional<Role> findOneByName(final String name);
	
}
