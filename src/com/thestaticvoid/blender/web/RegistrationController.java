package com.thestaticvoid.blender.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thestaticvoid.blender.service.RegistrationException;
import com.thestaticvoid.blender.service.UserService;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	private UserService userService;
	private ReCaptcha reCaptcha;
	private MessageSource messageSource;

	@Autowired
	public RegistrationController(UserService userService, ReCaptcha reCaptcha, MessageSource messageSource) {
		this.userService = userService;
		this.reCaptcha = reCaptcha;
		this.messageSource = messageSource;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void register(Model model, HttpSession session) {
		model.addAttribute(new RegistrationForm());
		model.addAttribute("reCaptchaHtml", reCaptcha.createRecaptchaHtml(null, null));
		session.removeAttribute("isHuman");
	}

	@RequestMapping(method = RequestMethod.POST)
	public String register(RegistrationForm registrationForm,
			BindingResult result,
			@RequestParam(value = "recaptcha_challenge_field", required = false) String challenge,
			@RequestParam(value = "recaptcha_response_field", required = false) String response,
			Model model,
			HttpServletRequest request,
			HttpSession session) {
		
		// check that the password confirmation matches the password
		if (registrationForm.getPasswordConfirmation() == null || registrationForm.getPasswordConfirmation().equals(""))
			result.rejectValue("passwordConfirmation", "field.required");
		else if (!registrationForm.getPasswordConfirmation().equals(registrationForm.getPassword()))
			result.rejectValue("passwordConfirmation", "passwords.nomatch");
		
		// check CAPTCHA if we haven't successfully before
		if (session.getAttribute("isHuman") == null) {
			boolean isValid;
			try {
				ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(), challenge, response);
				isValid = reCaptchaResponse.isValid();
			} catch (Exception e) {
				isValid = false;
			}

			if (isValid)
				session.setAttribute("isHuman", true);
			else {
				result.rejectValue("reCaptcha", "captcha.incorrect");
				model.addAttribute("reCaptchaHtml", reCaptcha.createRecaptchaHtml(null, null));
			}
		}
		
		// try to register user if we didn't have any errors above
		// otherwise just check for any further form errors
		try {
			userService.registerUser(registrationForm, result.hasErrors());
		} catch (RegistrationException re) {
			Map<String, String> errors = re.getErrors();
			for (String path : errors.keySet())
				result.rejectValue(path, errors.get(path));
		}
		
		// send user back to the form to fix any validation errors
		if (result.hasErrors()) {
			if (session.getAttribute("isHuman") != null)
				model.addAttribute("reCaptchaHtml", messageSource.getMessage("captcha.correct", null, null));
			
			return "register";
		}
		
		// prevent multiple automated registrations by requiring
		// CAPTCHA for each registration
		session.removeAttribute("isHuman");
		
		return "redirect:registerSuccess";
	}

	@RequestMapping("/checkUsernameAvailable")
	public @ResponseBody boolean checkUsernameAvailable(String username) {
		return !userService.userExists(username);
	}

	@RequestMapping("/checkEmailNotRegistered")
	public @ResponseBody boolean checkEmailNotRegistered(String email) {
		return !userService.emailRegistered(email);
	}

	@RequestMapping("/checkReCaptchaValid")
	public @ResponseBody boolean checkReCaptchaValid(String challenge, String response, HttpServletRequest request, HttpSession session) {
		try {
			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(), challenge, response);
			if (reCaptchaResponse.isValid()) {
				session.setAttribute("isHuman", true);
				return true;
			}
		} catch (Exception e) {
			// empty
		}

		return false;
	}
}
