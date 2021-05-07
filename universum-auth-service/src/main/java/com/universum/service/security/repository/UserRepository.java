package com.universum.service.security.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.universum.service.security.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	public Page<User> findAll(final Pageable pageable);
	
	public long countByUsername(final String userName);
	
	@EntityGraph(attributePaths = "roles")
	public Optional<User> findOneWithRolesByUsername(final String userName);
	
	public Optional<User> findOneByUsername(final String userName);
	
	@EntityGraph(attributePaths = "roles")
	public Optional<User> findOneWithRolesById(final Long id);
	
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE app_user SET last_successful_login_time = :lastSuccessfulLoginTime, failed_login_attempts = :failedLoginAttempts WHERE username = :userName")
	public int updateOnLoginSuccess(@Param("lastSuccessfulLoginTime") final LocalDateTime roleId, @Param("failedLoginAttempts") final int failedLoginAttempts, @Param("userName") final String userName);
	
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE app_user SET last_login_failure_time = :lastLoginFailureTime, failed_login_attempts = failed_login_attempts+1 WHERE username = :userName")
	public int updateOnLoginFailure(@Param("lastLoginFailureTime") final LocalDateTime roleId, @Param("userName") final String userName);
}
