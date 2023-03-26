package org.isfce.pid.controller;

import javax.security.auth.login.CredentialException;

import org.isfce.pid.controller.dto.ChangePasswordDto;
import org.isfce.pid.controller.dto.CredentialValidation;
import org.isfce.pid.controller.exceptions.NotExistException;
import org.isfce.pid.model.Roles;
import org.isfce.pid.model.User;
import org.isfce.pid.service.UserServices;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserServices userService;

	/**
	 * @param userSrv autoinjecté
	 */
	public UserController(UserServices userSrv, PasswordEncoder ncodeur) {
		this.userService = userSrv;
	}

	@GetMapping("/{username}/update")
	public String changePWGet(@PathVariable String username, Authentication auth, Model model) {
		// vérifie si le user existe
		User user = userService.findById(username)
				.orElseThrow(() -> new NotExistException(username));
		// vérifie que c'est bien l'utilisateur connecté
		// Vérifie s'il a les droits admin ou prof loggé sinon déclenche exception 403
		if (auth == null || (!(auth.getName().equals(user.getUsername()) || hasRole(auth, Roles.ROLE_ADMIN))))
			throw new AccessDeniedException("Access Denied: Failed");

		ChangePasswordDto cpd = ChangePasswordDto.createPwDto(user);
		model.addAttribute("userDto", cpd);
		return "user/userChangePw";
	}

	@PostMapping("/{username}/update")
	public String changePWPost(@PathVariable String username,
			@Validated(CredentialValidation.class) @ModelAttribute("userDto") ChangePasswordDto userCPwDto,
			BindingResult errors, Authentication auth, Model model) {
		// vérifie si le user existe
		User user = userService.findById(username)
				.orElseThrow(() -> new NotExistException(username));

		// retour à la vue si erreurs
		if (errors.hasErrors()) {
			return "user/userChangePw";
		}
		// change le pw
		try {
			userService.changePassword(user, userCPwDto.getOldPassword(), userCPwDto.getPassword());
		} catch (CredentialException e) {
			errors.reject("user.badPassword", "Erreur X");
			return "user/userChangePw";
		}

		return "redirect:/";
	}
	
	
	/**
	 * 
	 * Permet de vérifier si un username existe déjà actuellement en mode Get, on
	 * peut la transformer en Post pour éviter que n'importe qui teste l'existance
	 * des usernames
	 * 
	 * @param username
	 * @return true ou false
	 */
	@GetMapping("/check/{username}")
	public @ResponseBody boolean isValid(@PathVariable("username") String username) {
		return userService.existsById(username);
	}

	/**
	 * Petite méthode privée qui vérifie si l'objet auth possède le role désigné
	 * 
	 * @param auth
	 * @param role
	 * @return vrai s'il possède le role
	 */
	private boolean hasRole(Authentication auth, Roles role) {
		String roleStr = role.name();
		return auth.getAuthorities().stream().anyMatch(a -> roleStr.equals(a.getAuthority()));
	}
}
