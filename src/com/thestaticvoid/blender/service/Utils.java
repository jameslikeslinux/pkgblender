package com.thestaticvoid.blender.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class Utils {
	public static <T> Map<String, String> validate(Validator validator, T object) {
		Map<String, String> errors = new HashMap<String, String>();
		Set<ConstraintViolation<T>> violations = validator.validate(object);
		
		for (ConstraintViolation<T> violation : violations) {
			String path = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.put(path, message);
		}
		
		return errors;
	}
}
