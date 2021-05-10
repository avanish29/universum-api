package com.universum.security.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.universum.security.util.AuthenticationConstant;

public class SecurityAuditAware implements AuditorAware<String>{
	public static final String SECURITY_AUDIT_AWARE = "securityAuditAware";

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(getCurrentLoginUser().orElse(AuthenticationConstant.SYSTEM_ACCOUNT));
	}
	
	public Optional<String> getCurrentLoginUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails)authentication.getPrincipal();
				return userDetails.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				return (String)authentication.getPrincipal();
			}
			return null;
		});
	}

}
