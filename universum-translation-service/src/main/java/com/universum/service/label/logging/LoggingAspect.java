package com.universum.service.label.logging;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Pointcut("within(@org.springframework.stereotype.Repository *)" +
	        " || within(@org.springframework.stereotype.Service *)" +
	        " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springBeanPointCut() {
		// Empty pointcut for spring package
	}
	
	@Pointcut("within(com.universum.security..*)" +
			" || within(com.universum.common..*)" +
			" || within(com.universum.service.label..*)")
	public void applicationPointCut() {
		// Empty pointcut for application package
	}
	
	@AfterThrowing(pointcut = "applicationPointCut() && springBeanPointCut()", throwing = "ex")
	public void logErrorAfterException(JoinPoint joinPoint, Throwable ex) {
		LOGGER.error("Exception in class = {} Method = {}() with cause = '{}' and message = '{}'", 
				joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), 
				ex.getCause() != null ? ex.getCause() : "NULL CAUSE", ex.getMessage(), ex);
	}
	
	@Around("applicationPointCut() && springBeanPointCut()")
	public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
	                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		}
		try {
			Object result = joinPoint.proceed();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);
            }
            return result;
		} catch (IllegalArgumentException ex) {
			LOGGER.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw ex;
        }
	}
}
