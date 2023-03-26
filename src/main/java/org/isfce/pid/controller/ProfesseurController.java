package org.isfce.pid.controller;

import javax.validation.groups.Default;

import org.isfce.pid.controller.dto.CredentialValidation;
import org.isfce.pid.controller.dto.ProfesseurDto;
import org.isfce.pid.controller.exceptions.NotExistException;
import org.isfce.pid.model.Professeur;
import org.isfce.pid.model.Roles;
import org.isfce.pid.service.ProfesseurServices;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Didier
 *
 */
@Slf4j
@Controller
@RequestMapping("/professeur")
public class ProfesseurController {

	PasswordEncoder encodeur;
	ProfesseurServices serviceProfesseurs;
	UserServices userService;

	/**
	 * Construction avec autowired des services et de l'encodeur
	 * 
	 * @param serviceProfesseurs
	 * @param userService
	 * @param encodeur
	 */
	public ProfesseurController(ProfesseurServices serviceProfesseurs, UserServices userService,
			PasswordEncoder encodeur) {
		this.serviceProfesseurs = serviceProfesseurs;
		this.userService = userService;
		this.encodeur = encodeur;
	}

	/**
	 * Liste des professeurs
	 * 
	 * @param model
	 * @return vue
	 */
	@GetMapping("/liste")
	public String getProfesseurs(Model model) {
		model.addAttribute("professeurList", serviceProfesseurs.findAll());
		return "professeur/listeProfesseur";
	}

	/**
	 * Détail d'un professeur à partir d'un paramètre username
	 * 
	 * @param username le login de l'utilisateur
	 * @param model
	 * @return la vue du professeur
	 */
	@GetMapping("/")
	public String getProfesseur(@RequestParam("username") String username, Model model, Authentication auth) {
		log.info("Professeur par username: " + username);
		// Verifie si pas dans la Map suite à une redirection avec FlashAttribut
		if (!model.containsAttribute("professeur")) {
			// Récupère le professeur sinon déclenche une exception
			Professeur prof = serviceProfesseurs.getByUserName(username)
					.orElseThrow(() -> new NotExistException(username));
			// Vérifie s'il a les droits admin ou prof 
			if (auth == null || !(hasRole(auth, Roles.ROLE_PROF) || hasRole(auth, Roles.ROLE_ADMIN)))
				throw new AccessDeniedException("Access Denied: Failed");
			model.addAttribute("professeur", prof);
		}
		return "professeur/professeur";
	}

	/**
	 * Détail d'un prof à partir de son id via une 'path' variable
	 * 
	 * @param id
	 * @param model
	 * @param auth
	 * @return la vue du professeur
	 */
	@GetMapping("/{id}")
	public String getProfesseur(@PathVariable Integer id, Model model, Authentication auth) {
		log.info("Professeur par id: " + id);
		// Verifie si pas dans la Map suite à d'une redirection avec FlashAttribut
		if (!model.containsAttribute("professeur")) { // Récupère le professeur sinon déclenche une exception
			Professeur prof = serviceProfesseurs.getById(id).orElseThrow(() -> new NotExistException(id.toString()));
			// Vérifie s'il a les droits admin ou prof 
			if (auth == null
					|| !(hasRole(auth, Roles.ROLE_PROF) || hasRole(auth, Roles.ROLE_ADMIN)))
				throw new AccessDeniedException("Access Denied: Failed");

			model.addAttribute("professeur", prof);
		}
		return "professeur/professeur";
	}

	/**
	 * Méthode Get pour ajouter un Professeur
	 * 
	 * @param prof
	 * @param model
	 * @return
	 */

	@GetMapping("/add")
	public String addProfesseurGet(@ModelAttribute("professeur") ProfesseurDto prof, Model model) {
		log.debug("affiche la vue pour ajouter un professeur ");
		return "professeur/addProfesseur";
	}

	/**
	 * Post pour Ajout d'un professeur Ici on fait tous les tests d'erreur avant
	 * l'ajout ==> plusieurs accès à la bd mais en présence de plusieurs types
	 * d'erreur, on revient à la vue avec toutes celle-ci (ça évite de revenir
	 * plusieurs fois sur une même vue avec des erreurs)
	 * 
	 * @param profDto
	 * @param errors
	 * @return
	 */
	@PostMapping("/add")
	public String addProfesseurPost(@Validated({ CredentialValidation.class,
			Default.class }) @ModelAttribute(name = "professeur") ProfesseurDto profDto, BindingResult errors) {
		log.debug(" Post Professeur");

		// Vérification doublon sur le username
		if (userService.existsById(profDto.getUser().getUsername()))
			errors.rejectValue("user.username", "user.username.doubon", "Existe déjà!");

		// Verification sur la contrainte unique nom et prénom
		if (serviceProfesseurs.existsByNomPrenom(profDto.getNom(), profDto.getPrenom()))
			errors.reject("professeur.nom.doublon", "Existe déjà!");

		// Gestion de la validation avec toutes les erreurs de données possibles
		if (errors.hasErrors()) {
			log.debug("Erreurs dans les données:" + profDto.getId());
			return "professeur/addProfesseur";
		}

		// Conversion Dto==>Prof et crypte son pw
		Professeur prof = profDto.toProfesseur(encodeur);
		try {
			// Sauvegarde
			prof = serviceProfesseurs.save(prof);
		} catch (Exception e) {
			log.debug("Erreur Lors de la sauvagarde: ", e.getMessage());
			// problème technique
			errors.reject("error.persistance", "Exception de persistance");
			return "professeur/addProfesseur";
		}

		return "redirect:/professeur/liste";
	}

	/**
	 * Get Affiche la vue pour faire un update d'un professeur un dto de prof sera
	 * envoyé avec les passwords effacés
	 * 
	 * @param id
	 * @param model
	 * @param auth
	 * @return
	 */
	// Méthode Get pour faire un update d'un Professeur
	@GetMapping("/{id}/update")
	public String updateProfesseurGet(@PathVariable Integer id, Model model, Authentication auth) {
		log.debug("affiche la vue pour modifier un cours:" + id);

		// Récupère le professeur sinon déclenche une exception
		Professeur prof = serviceProfesseurs.getById(id).orElseThrow(() -> new NotExistException(id.toString()));

		// Vérifie les droits
		// As-t-on le droit de modifier ce professeur?
		if (auth == null || !(prof.getUser().getUsername().equals(auth.getName()) || hasRole(auth, Roles.ROLE_ADMIN)))
			throw new AccessDeniedException("Access Denied: Failed");

		// charge un Dto
		ProfesseurDto profDto = ProfesseurDto.toDto(prof);

		// Pw bidon efface les password (cryptés)
		profDto.getUser().setPassword("okPassword1");
		profDto.getUser().setConfirmPassword("okPassword1");

		// Ajout au Modèle
		model.addAttribute("professeur", profDto);

		return "professeur/updateProfesseur";
	}

	/**
	 * Post réception de l'update d'un prof à partir d'un dto. Seules les données du
	 * professeur (sauf id) seront modifiables la validation ne testera pas les
	 * données professeur.user du dto si tout va bien le professeur sera modifié et
	 * on retourne à la liste des profs
	 * 
	 * @param id      du prof
	 * @param profDto le dto du prof mais sans valider le user
	 * @param errors  les erreurs mais pas celles du user
	 * @param model
	 * @param rModel  modèle pour une redirection
	 * @return
	 */
	@PostMapping("{id}/update")
	public String updateCoursPost(@PathVariable Integer id,
			@ModelAttribute("professeur") @Validated(Default.class) ProfesseurDto profDto, BindingResult errors,
			Authentication auth, RedirectAttributes rModel) {

		log.debug("Post update Professeur");
		// Gestion de la validation
		if (errors.hasErrors()) {
			log.debug("Erreurs dans les données:" + profDto.getId());
			return "professeur/updateProfesseur";
		}

		// Récupère le professeur et déclenche une exception s'il n'existe pas
		Professeur prof = serviceProfesseurs.getById(id).orElseThrow(() -> new NotExistException(id.toString()));

		// Vérifie les droits
		// As-t-on le droit de modifier ce professeur?
		if (auth == null || !(prof.getUser().getUsername().equals(auth.getName()) || hasRole(auth, Roles.ROLE_ADMIN)))
			throw new AccessDeniedException("Access Denied: Failed");

		// si le nom et ou le prenom ont changé, teste s'il n'existe pas de doublon:
		if (!prof.getNom().equals(profDto.getNom()) || !prof.getPrenom().equals(profDto.getPrenom()))
			if (serviceProfesseurs.existsByNomPrenom(profDto.getNom(), profDto.getPrenom())) {
				errors.reject("professeur.nom.doublon", "Existe déjà!");
				return "professeur/updateProfesseur";
			}

		// Maj du professeur actuel
		prof.setEmail(profDto.getEmail());
		prof.setNom(profDto.getNom());
		prof.setPrenom(profDto.getPrenom());

		try {
			prof = serviceProfesseurs.save(prof);
		} catch (Exception e) {
			log.debug("Erreur Lors de la sauvagarde: ", e.getMessage());
			// problème technique
			errors.reject("error.persistance", "Exception de persistance");
			return "professeur/updateProfesseur";
		}

		// Préparation des attributs Flash pour survivre à la redirection
		// Ainsi dans l'affichage du détail du prof on ne devra pas chercher
		// le prof dans la BD
		rModel.addFlashAttribute("professeur", prof);
		return "redirect:/professeur/" + id;
	}

	/**
	 * Supression d'un professeur
	 * 
	 * @param id du professeur
	 * @return le mapping de redirection
	 */
	@PostMapping("/{id}/delete")
	public String deleteProfesseur(@PathVariable Integer id) {

		if (this.serviceProfesseurs.existsById(id))
			serviceProfesseurs.deleteById(id);
		else
			throw new NotExistException(id.toString());

		log.debug("Supression du professeur: " + id);
		return "redirect:/professeur/liste";
	}

	// Permet de rajouter la liste des roles à chaque Model
	// @ModelAttribute("rolesAll")
	// private List<Roles> getRoles() {
	// return List.of(Roles.values());
	// }

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
