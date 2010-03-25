package com.thestaticvoid.blender.web;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thestaticvoid.blender.authentication.ReCaptchaAuthenticationFilter;

@Controller
@RequestMapping("/login")
public class LoginController {
	private ReCaptcha reCaptcha;
	
	@Autowired
	public LoginController(ReCaptcha reCaptcha) {
		this.reCaptcha = reCaptcha;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public void login(Model model, HttpServletRequest request) {
		if (ReCaptchaAuthenticationFilter.requireCaptcha(request.getRemoteAddr()))
			model.addAttribute("reCaptchaHtml", reCaptcha.createRecaptchaHtml(null, null));
	}
}
