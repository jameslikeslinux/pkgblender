package com.thestaticvoid.blender.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thestaticvoid.blender.service.AdministrationService;
import com.thestaticvoid.blender.service.OsDetails;
import com.thestaticvoid.blender.service.ValidationException;

@Controller
public class AdministrationController {
	@Autowired
	private AdministrationService administrationService;
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/admin/oses", method = RequestMethod.GET)
	public String adminOses(Model model) {
		model.addAttribute("oses", administrationService.getOses());
		model.addAttribute("newOsForm", new OsDetails());
		return "admin/oses";
	}
	
	@RequestMapping(value = "/admin/oses", method = RequestMethod.POST)
	public String adminOses(@ModelAttribute("newOsForm") OsDetails newOsForm, BindingResult result, Model model) {
		try {
			administrationService.createOs(newOsForm);
		} catch (ValidationException ve) {
			Map<String, String> errors = ve.getErrors();
			for (String path : errors.keySet())
				result.rejectValue(path, errors.get(path));
		}
		
		if (result.hasErrors()) {
			model.addAttribute("oses", administrationService.getOses());
			return "admin/oses";
		}
		
		return "redirect:/admin/oses";
	}
	
	@RequestMapping(value = "/admin/buildHosts", method = RequestMethod.GET)
	public String adminBuildHosts(Model model) {
		BuildHostForm newBuildHostForm = new BuildHostForm();
		newBuildHostForm.setOses(administrationService.getOses());
		model.addAttribute("newBuildHostForm", newBuildHostForm);
		return "admin/buildHosts";
	}
}
