package com.thestaticvoid.blender.service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.EmailValidationToken;
import com.thestaticvoid.blender.domain.EmailValidationTokenDao;
import com.thestaticvoid.blender.domain.Role;
import com.thestaticvoid.blender.domain.User;
import com.thestaticvoid.blender.domain.UserDao;

@Service("userService")
public class UserServiceImpl implements UserService {
	private UserDao userDao;
	private EmailValidationTokenDao emailValidationTokenDao;
	private Validator validator;
	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;
	private MailSender mailSender;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setEmailValidationTokenDao(EmailValidationTokenDao emailValidationTokenDao) {
		this.emailValidationTokenDao = emailValidationTokenDao;
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
	
	@Autowired
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Transactional(readOnly = true)
	public boolean userExists(String username) {
		return userDao.getByUsername(username) != null;
	}
	
	@Transactional(readOnly = true)
	public boolean emailRegistered(String email) {
		return userDao.getByEmail(email) != null;
	}
	
	private static String generateToken() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xff & bytes[i]);
			
			if (hex.length() == 1)
				buffer.append("0");
			
			buffer.append(hex);
		}
		
		return buffer.toString();
	}
	
	@Transactional
	private void sendEmailVerification(User user) {
		EmailValidationToken emailValidationToken = user.getEmailValidationToken();
		String token = generateToken();
		
		if (emailValidationToken == null) {
			emailValidationToken = new EmailValidationToken();
			emailValidationToken.setToken(token);
			emailValidationToken.setUser(user);
			emailValidationTokenDao.store(emailValidationToken);
		} else
			emailValidationToken.setToken(token);
		
		// send an email reflecting the new token
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("system@pkgblender.org");
		msg.setTo(user.getEmail());
		msg.setSubject("[pkgblender] Please Verify Your Email Address");
		msg.setText(
			user.getName() + ",\n\n" +
			"You have registered at pkgblender.org.  Please verify your email\n" +
			"address by clicking the link below:\n\n" +
			"    http://localhost:8080/blender/verifyEmail?token=" + token + "\n\n" +
			"Thank you.");
		
		mailSender.send(msg);
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
		
		// store the new user in the database
		userDao.store(newUser);
		
		// send the user an email to verify their address
		sendEmailVerification(newUser);
		
		// update the newUser object with the newly created email address token
		userDao.refresh(newUser);
		
		// login the user (caches the newUser object in the Spring SecurityContext)
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(newUser, details.getPassword(), newUser.getAuthorities());
		token.setDetails(newUser);
		SecurityContextHolder.getContext().setAuthentication(token);
		newUser.setLastLogin(new Date());
	}
	
	@Transactional
	public void resendEmailVerification() {
		User user = userDao.get(Utils.getCachedUser().getId());
		sendEmailVerification(user);
	}
	
	@Transactional(noRollbackFor = EmailVerificationException.class)
	public void verifyEmail(String token) {
		User user = userDao.get(Utils.getCachedUser().getId());
		
		if (user.isEmailValid())
			throw new EmailVerificationException("email.already.verified");
		
		if (emailRegistered(user.getEmail()))
			throw new EmailVerificationException("email.registered.before.verification");
		
		if (!user.getEmailValidationToken().getToken().equals(token)) {
			sendEmailVerification(user);
			throw new EmailVerificationException("email.bad.validation.token");
		}
		
		// the validation was successful so remove the token from the database
		// and the cached user (in the Spring SecurityContext)
		emailValidationTokenDao.remove(user.getEmailValidationToken());
		Utils.getCachedUser().setEmailValidationToken(null);
	}

	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		User user = userDao.getByUsername(username);

		if (user == null)
			throw new UsernameNotFoundException("Not found.", username);
		
		user.setLastLogin(new Date());
		
		return user;
	}
}