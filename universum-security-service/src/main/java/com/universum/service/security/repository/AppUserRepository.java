package com.universum.service.security.repository;

import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universum.common.exception.NotFoundException;
import com.universum.service.security.entity.ApplicationUser;

@Repository
public interface AppUserRepository extends JpaRepository<ApplicationUser, Long>{
	public Page<ApplicationUser> findAllByDeletedFalse(final Pageable pageable);
	
	public Optional<ApplicationUser> findByUsernameAndDeletedFalse(final String userName);
	
	public default ApplicationUser findByIdNotDeleted(final Long id) {
		Optional<ApplicationUser> userById = findById(id);
		if(userById.isEmpty() || BooleanUtils.isTrue(userById.get().getDeleted())) {
			throw new NotFoundException(ApplicationUser.class, id);
		}
		return userById.get();
	}
}
