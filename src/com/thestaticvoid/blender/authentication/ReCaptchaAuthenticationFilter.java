package com.thestaticvoid.blender.authentication;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class ReCaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static int MAX_BAD_LOGINS = 4;
	private static Map<String, Integer> badLoginsByIp = Collections.synchronizedMap(new HashMap<String, Integer>());
	
	private ReCaptcha reCaptcha;
	
	public void setReCaptcha(ReCaptcha reCaptcha) {
		this.reCaptcha = reCaptcha;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		
		Authentication authentication = super.attemptAuthentication(request, response);
		
		if (requireCaptcha(request.getRemoteAddr())) {
			boolean isValid;
			try {
				ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
				isValid = reCaptchaResponse.isValid();
			} catch (Exception e) {
				isValid = false;
			}

			if (!isValid)
				throw new ReCaptchaInvalidException();
		}
		
		return authentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {
		
		super.successfulAuthentication(request, response, authResult);
		
		badLoginsByIp.remove(request.getRemoteAddr());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		
		super.unsuccessfulAuthentication(request, response, failed);
		
		Integer badLogins = badLoginsByIp.get(request.getRemoteAddr());
		badLogins = badLogins == null ? 1 : badLogins + 1;
		badLoginsByIp.put(request.getRemoteAddr(), badLogins);
	}
	
	public static boolean requireCaptcha(String remoteAddr) {
		Integer badLogins = badLoginsByIp.get(remoteAddr);
		return (badLogins != null && badLogins > MAX_BAD_LOGINS);
	}
}
