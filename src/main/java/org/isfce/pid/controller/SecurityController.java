package org.isfce.pid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class SecurityController {
	@GetMapping("/login")
    public String login() {
        log.info("Login with SecurityController");
        return "login";
    } 
	
	// Login form with error
	  @GetMapping("/login-error")
	  public String loginError(Model model) {  
		  log.warn("Login erreur with SecurityController");
	    model.addAttribute("loginError", true);
	    return "login";
	  }
}
