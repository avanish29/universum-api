package com.universum.common.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.universum.common.validator.constraint.ValidEnum;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
	private List<String> valueList = new ArrayList<>();
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return valueList.contains(value.toUpperCase());
	}
	
	@Override
	public void initialize(final ValidEnum constraintAnnotation) {
		Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
		Enum<?>[] enumVals = enumClass.getEnumConstants();
		valueList = Arrays.stream(enumVals).map(enumVal -> enumVal.toString().toUpperCase()).collect(Collectors.toList());
	}

}
