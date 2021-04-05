package com.universum.service.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universum.service.security.entity.ApplicationUser;

@Repository
public interface AppUserRepository extends JpaRepository<ApplicationUser, Long>{
	public Page<ApplicationUser> findAllByDeletedFalse(final Pageable pageable);
	
	public long countByUsername(final String userName);
	
	@EntityGraph(attributePaths = "roles")
	public Optional<ApplicationUser> findOneWithRolesByUsernameAndDeletedFalse(final String userName);
	
	public Optional<ApplicationUser> findOneByUsername(final String userName);
	
	@EntityGraph(attributePaths = "roles")
	public Optional<ApplicationUser> findOneWithRolesById(final Long id);
	
}
