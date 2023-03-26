package org.isfce.pid.controller;

import java.security.Principal;
import java.util.Optional;

import org.isfce.pid.model.Professeur;
import org.isfce.pid.model.Roles;
import org.isfce.pid.model.User;
import org.isfce.pid.service.CoursServices;
import org.isfce.pid.service.ModuleServices;
import org.isfce.pid.service.ProfesseurServices;
import org.isfce.pid.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class Home {
	@Value("${spring.thymeleaf.encoding}")
	String thyme1;
	
	private ProfesseurServices profServices;
	
	// injection de l'accès au service
	@Autowired
	public Home(ProfesseurServices profServices) {
		this.profServices = profServices;
	}

	@GetMapping("/")
	public String home(Model model, Principal principal) {
		if (principal != null)
		{
			log.debug(principal.getName());
			model.addAttribute("prof", "VO");
			Optional<Professeur> professeur = profServices.getByUserName(principal.getName());
			if (professeur.isPresent())
			{
				log.debug("+++++++++++++++++++++ Prof est present");
				model.addAttribute("username", professeur.get().getUser().getUsername());
			}
			else
			{
				model.addAttribute("username", principal.getName());
			}
		}
		
		model.addAttribute("cours", "PID");
		model.addAttribute("thyme", thyme1);
		return "home";
	}

	@GetMapping("/test")
	public @ResponseBody Professeur restMethode() {
		return new Professeur("VO", "Van Oudenhove", "Didier", new User("vo", "vo", Roles.ROLE_PROF));
	}

}
