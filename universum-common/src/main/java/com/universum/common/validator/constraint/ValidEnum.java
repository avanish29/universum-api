package com.universum.common.validator.constraint;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;

import com.universum.common.validator.EnumValidator;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotBlank(message = "Value cannot be null or empty.")
@ReportAsSingleViolation
public @interface ValidEnum {
	Class<? extends Enum<?>> enumClass();
	
	String message() default "Not a valid value.";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	@Target(ElementType.FIELD)
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		ValidEnum[] value();
	}
}
