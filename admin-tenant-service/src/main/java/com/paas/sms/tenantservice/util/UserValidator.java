package com.paas.sms.tenantservice.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.paas.sms.tenantservice.document.User;
import com.paas.sms.tenantservice.service.UserService;

@Component
public class UserValidator implements Validator{

	private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);
	
	@Autowired
	private UserService userService;

	
	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		logger.info("Enter UserValidator.validate( )");

		User user = (User) o;

		logger.info("User in validate method" + user.getUsername());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
		if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
			errors.rejectValue("username", "Size.userForm.username");
		}
		if (userService.search(user.getUsername()) != null) {
			errors.rejectValue("username", "Duplicate.userForm.username");
		}

		logger.info("User in validate method" + user.getPassword());
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

		if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.userForm.password");
		}

		if (!user.getPasswordConfirm().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
		}

	}	

}
	
