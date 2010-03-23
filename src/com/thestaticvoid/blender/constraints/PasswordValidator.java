package com.thestaticvoid.blender.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
	public void initialize(Password passwordAnnotation) {
		// empty
	}
	
	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if (object == null || object.length() == 0)
			return true;
		
		if (object.length() >= 8 && object.matches(".*[a-z].*") && object.matches(".*[A-Z].*") && object.matches(".*[0-9].*"))
			return true;
		
		return false;
	}
}
