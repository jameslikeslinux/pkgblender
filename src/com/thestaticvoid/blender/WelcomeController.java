package com.thestaticvoid.blender;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class WelcomeController {
	private PersonDao personDao;
	
	@Autowired
	public WelcomeController(PersonDao personDao) {
		this.personDao = personDao;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String welcome(Model model) {
		model.addAttribute("today", new Date());
		model.addAttribute("people", personDao.findAll());
		model.addAttribute(new Person());
		return "welcome"; 
	}
	
	@RequestMapping(value = "/delete/{personId}", method = RequestMethod.GET)
	public String remove(@PathVariable int personId, Model model) {
		personDao.delete(personId);
		return "redirect:/";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String create(Person person) {
		personDao.store(person);
		return "redirect:/";
	}
}