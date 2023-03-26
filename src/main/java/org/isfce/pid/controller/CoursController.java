package org.isfce.pid.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.isfce.pid.controller.dto.CoursDto;
import org.isfce.pid.controller.exceptions.NotExistException;
import org.isfce.pid.model.Cours;
import org.isfce.pid.service.CoursServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/cours")
public class CoursController {

	private CoursServices coursService;

	// injection de l'accès au service
	@Autowired
	public CoursController(CoursServices coursService) {
		this.coursService = coursService;
	}

	/**
	 * Liste des cours
	 * 
	 * @param model
	 * @return la nom logique de la vue qui affichera la liste des cours
	 */
	@GetMapping("/liste")
	public String listeCours(Model model) {
		model.addAttribute("coursList", coursService.findAll());
		model.addAttribute("ecole", "ISFCE");
		return "cours/listeCours";
	}

	/**
	 * Methode Get pour ajouter un cours
	 * 
	 * @param cours un cours avec des valeurs par défaut
	 * @return nom logique de la vue pour créer un cours
	 */
	@GetMapping("/add")
	public String addCoursGet(@ModelAttribute("cours") CoursDto cours) {
		log.debug("affiche la vue pour ajouter un cours ");
		return "cours/addCours";
	}

	/**
	 * Méthode Post pour créer le nouveau cours
	 * 
	 * @param cours  le cours
	 * @param errors les erreurs de validation
	 * @param rModel une map qui peut survivre à une redirection
	 * @return une URL de redirection
	 */
	@PostMapping("/add")
	public String addCoursPost(@Valid @ModelAttribute("cours") CoursDto coursDto, BindingResult errors,
			RedirectAttributes rModel) {
		log.info("POST d'un cours" + coursDto);
		// Gestion de la validation en fonction des annotations
		if (errors.hasErrors()) {
			log.debug("Erreurs dans les données du cours:" + coursDto.getCode());
			return "cours/addCours";
		}

		// Vérifie que ce cours n'existe pas encore
		if (coursService.exists(coursDto.getCode())) {
			errors.rejectValue("code", "cours.code.doublon", "DUPLICATE ERROR");
			return "cours/addCours";
		}

		coursService.insert(coursDto.toCours());

		// Préparation des attributs Flash pour survivre à la redirection
		// Ainsi dans l'affichage du détail du cours on ne devra pas chercher
		// le cours dans la BD
		rModel.addFlashAttribute("cours", coursDto);

		log.debug("redirection cours/liste:");
		return "redirect:/cours/" + coursDto.getCode();
	}

	/**
	 * Méthode Get pour afficher la page d'édition d'un cours
	 * @param code :le code du cours
	 * @param model auquel sera ajouté le savedID et le cours à modifier
	 * @return la vue logique 
	 */
		@GetMapping("/{cours}/update")
		public String updateCoursGet(@PathVariable(name = "cours") String code, Model model) {
			// rajoute le coursDTO ou déclenche une exception
			model.addAttribute("cours", CoursDto.toDto(coursService.findOne(code.toUpperCase()).orElseThrow(()->new NotExistException(code))));
			// rajoute l'id du cours pour gérer la maj de ce dernier (problème de maj de la PK)
			model.addAttribute("savedId", code);
			log.debug("affiche la vue pour modifier un cours ");
			return "cours/updateCours";
		}

	/**
	 * 
	 * @param code     désigne le code du cours avant modification
	 * @param coursDto l'objet cours modifié
	 * @param errors   les erreurs de validation
	 * @param model    pour rajouter des attributs
	 * @param rModel   le modèle pour la redirection
	 * @return
	 */
	@PostMapping("{cours}/update")
	public String updateCoursPost(@PathVariable(name = "cours") String idCours,
			@ModelAttribute("cours") @Valid CoursDto coursDto, BindingResult errors, Model model,
			RedirectAttributes rModel) {
		log.info("POST update d'un cours  Code: " + idCours + " en " + coursDto);
		if (idCours == null)
			throw new NotExistException("null");
		
		// Gestion de la validation en fonction des annotations
		if (errors.hasErrors()) {
			// rajoute idCours pour savoir le code initial du cours
			model.addAttribute("savedId", idCours);
			log.info("Erreurs dans les données du cours:" + coursDto.getCode());
			return "cours/updateCours";
		}
		// vérifie que l'id n'a pas changé et fait un update
		if (coursDto.getCode().equals(idCours))
			coursService.update(coursDto.toCours());
		else // si le code a changé vérifie que le nouveau code n'existe pas déjà
		if (coursService.exists(coursDto.getCode())) {
			// rajoute idCours pour savoir le code initial du cours
			model.addAttribute("savedId", idCours);
			errors.rejectValue("code", "cours.code.doublon", "DUPLICATE ERROR");
			return "cours/updateCours";
		} else { // supprime l'objet avec l'ancien code et rajoute le nouveau
			coursService.delete(idCours);
			coursService.insert(coursDto.toCours());
		}

		// Préparation des attributs Flash pour survivre à  la redirection
		// Ainsi dans l'affichage du détail du cours on ne devra pas chercher
		// le cours dans la BD
		
		rModel.addFlashAttribute("cours", coursDto);

		log.debug("redirection détail du cours");
		return "redirect:/cours/" + coursDto.getCode();
	}

	/**
	 * Supression d'un cours
	 * 
	 * @param code du cours
	 * @return le mapping de redirection
	 */
	@PostMapping("/{code}/delete")
	public String deleteCours(@PathVariable String code) {
		if (coursService.exists(code))
			coursService.delete(code);
		else
			throw new NotExistException(code);
		log.debug("Suppression réussie du cours: " + code);
		return "redirect:/cours/liste";
	}

	//
	/**
	 * Affiche le détail d'un cours
	 * 
	 * @param code
	 * @param model
	 * @param locale
	 * @return
	 */
	@GetMapping("/{code}")
	public String detailCours(@PathVariable String code, Model model) {
		log.debug("Recherche le cours: " + code);
		// si ce n'est pas une redirection, charge le cours
		if (!model.containsAttribute("cours"))
			// recherche le cours dans la liste et ajout au model
			model.addAttribute("cours",
					CoursDto.toDto(coursService.findOne(code).orElseThrow(() -> new NotExistException(code))));
		return "cours/cours";
	}

	/**
	 * Permet de rajouter un attribut "listeDesSections" sera insérer dans tous les
	 * "model" de ce controleur
	 * 
	 * @return une liste de sections non modifiable
	 */
	@ModelAttribute("listeDesSections")
	private List<String> getSection() {
		return List.of("Comptabilité", "Informatique", "Marketing", "Secrétariat");
	}

}
