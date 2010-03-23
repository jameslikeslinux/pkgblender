package com.thestaticvoid.blender.service;

import java.util.Collections;
import java.util.Map;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.Role;
import com.thestaticvoid.blender.domain.User;
import com.thestaticvoid.blender.domain.UserDao;

@Service("userService")
public class UserServiceImpl implements UserService {
	private UserDao userDao;
	private Validator validator;
	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	@Transactional(readOnly = true)
	public boolean userExists(String username) {
		return userDao.getByUsername(username) != null;
	}
	
	@Transactional(readOnly = true)
	public boolean emailRegistered(String email) {
		return userDao.getByEmail(email) != null;
	}
	
	@Transactional
	public void registerUser(RegistrationDetails details, boolean validateOnly) {
		Map<String, String> errors = Utils.validate(validator, details);
		
		if (userExists(details.getUsername()))
			errors.put("username", "username.already.registered");
		
		if (emailRegistered(details.getEmail()))
			errors.put("email", "email.already.registered");
		
		if (errors.size() > 0)
			throw new RegistrationException(errors);
		
		if (validateOnly)
			return;
		
		User newUser = new User();
		newUser.setUsername(details.getUsername());
		newUser.setEmail(details.getEmail());
		newUser.setName(details.getName());
		newUser.setRoles(Collections.singleton(Role.ROLE_USER));
		
		String passwordHash = passwordEncoder.encodePassword(details.getPassword(), saltSource.getSalt(newUser));
		newUser.setPassword(passwordHash);
		
		userDao.store(newUser);
	}

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserDetails userDetails = userDao.getByUsername(username);
		
		if (userDetails == null)
			throw new UsernameNotFoundException("Not found.", username);
			
		if (userDetails.getAuthorities().isEmpty())
			throw new UsernameNotFoundException("No granted authorities.", username);
		
		return userDetails;
	}
}
