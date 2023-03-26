package org.isfce.pid.controller;


import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.isfce.pid.controller.dto.ModulesDto;
import org.isfce.pid.controller.exceptions.CanNotDeleteException;
import org.isfce.pid.controller.exceptions.NotExistException;
import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Module;
import org.isfce.pid.model.Module.MAS;
import org.isfce.pid.model.Professeur;
import org.isfce.pid.service.CoursServices;
import org.isfce.pid.service.ModuleServices;
import org.isfce.pid.service.ProfesseurServices;
import org.isfce.pid.util.validation.LocalDateEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/module")
public class ModuleController {
	private ModuleServices moduleService;
	private ProfesseurServices professeurService;
	private CoursServices coursService;

	// injection de l'accès au service
	@Autowired
	public ModuleController(ModuleServices moduleService, ProfesseurServices professeurService, CoursServices coursService) {
		this.moduleService = moduleService;
		this.professeurService = professeurService;
		this.coursService = coursService;
	}
	
	@InitBinder
	public void bindingPreparation(WebDataBinder binder) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(formatter));
	}
	
	/**
	 * Liste des modules
	 * 
	 * @param model
	 * @return la nom logique de la vue qui affichera la liste des modules
	 */
	@GetMapping("/liste")
	public String listeModules(Model model) {
		model.addAttribute("modulesList", moduleService.findAll());
		return "module/listeModules";
	}
	
	/**
	 * Supression d'un modules
	 * 
	 * @param code du modules
	 * @return le mapping de redirection
	 * @throws CanNotDeleteException 
	 */
	@PostMapping("/delete/{code}")
	public String deleteModule(@PathVariable String code/*, BindingResult errors, Model model*/){
		log.debug("1111111111111111111111111111111111111111111111111111111");
		if (moduleService.exist(code))
		{
			log.debug("/+/++/+/+/+//+/+/+/+/+/+/+/+/+");
			try {
				moduleService.deleteModuleInscription(code);
			} catch (CanNotDeleteException e) {
				// TODO Auto-generated catch block
				return "redirect:/error";
			}
		}
		else
		{
			try {
				moduleService.delete(code);
			} catch (CanNotDeleteException e) {
				// TODO Auto-generated catch block
				throw new NotExistException(code);
			}
		}
		log.debug("Suppression réussie du modules: " + code);
		return "redirect:/module/liste";
	}
	
	/**
	 * Methode Get pour ajouter un modules
	 * 
	 * @param modules un modules avec des valeurs par défaut
	 * @return nom logique de la vue pour créer un modules
	 */
	@GetMapping("add/{code}")
	public String addModuleGet(@PathVariable String code, @ModelAttribute("module") ModulesDto modules, Model model) {
		log.debug("affiche la vue pour ajouter un module ");
		
		Optional<Cours> cours = coursService.findOne(code);
	    if (cours.isPresent())
	    {
	    	log.debug("Cours :  " + cours.get());
	        modules.setCoursCode(cours.get().getCode());
	        modules.setCode(moduleService.generateCodeModule(code));
	    }
	    else {
	    	log.debug("Cours Inexistant");
	        return "redirect:/error";
	    }
		// initialise la date de debut et la date de fin a la date actuel du calandrier
		modules.setDateDebut(LocalDate.now());
		modules.setDateFin(LocalDate.now());
		
		Map<String, MAS> moment = new HashMap<>();
		moment.put("MATIN", MAS.MATIN);
		moment.put("APM", MAS.APM);
		moment.put("SOIR", MAS.SOIR);
		
		List<String> usernames = this.professeurService.findAll().stream()
			    .map(professeur -> professeur.getUser().getUsername())
			    .collect(Collectors.toList());

		model.addAttribute("profs", usernames);
		model.addAttribute("mas", moment);
		return "module/addModule";
	}
	
	private void redirectionErreur(ModulesDto modules, Model rModel)
	{
		modules.setDateDebut(LocalDate.now());
		modules.setDateFin(LocalDate.now());
		
		Map<String, MAS> moment = new HashMap<>();
		moment.put("MATIN", MAS.MATIN);
		moment.put("APM", MAS.APM);
		moment.put("SOIR", MAS.SOIR);
		
		List<String> usernames = this.professeurService.findAll().stream()
			    .map(professeur -> professeur.getUser().getUsername())
			    .collect(Collectors.toList());

		rModel.addAttribute("module", modules);
		rModel.addAttribute("mas", moment);
		//rModel.addAttribute("profs", this.professeurService.findAll());
		rModel.addAttribute("profs", usernames);
	}

	/**
	 * Méthode Post pour créer le nouveau modules
	 * 
	 * @param modules  le modules
	 * @param errors les erreurs de validation
	 * @param rModel une map qui peut survivre à une redirection
	 * @return une URL de redirection
	 */
	@PostMapping("add/{code}")
	public String addModulePost(@PathVariable String code, @Valid @ModelAttribute("module") ModulesDto modules, BindingResult errors,
			Model rModel) {
		log.info("POST d'un module" + modules);
		
		// Gestion de la validation en fonction des annotations
		if (errors.hasErrors()) {
			log.debug("Erreurs dans les données du module:" + modules.getCode() + "; cours:" + modules.getCoursCode());
			redirectionErreur(modules, rModel);
			return "module/addModule";
		}
		
		String coursCode = modules.getCoursCode();
		Optional<Cours> cours = coursService.findOne(coursCode);

		// Vérifie que le cours est correct et existe
		if (!code.equals(coursCode) || !cours.isPresent()) {
			errors.rejectValue("coursCode", "module.coursCode.notvalid", "COURS ERROR (se cours est incorrect) : ");
			redirectionErreur(modules, rModel);
			return "module/addModule";
		}
		
		Optional<Professeur> professeurs = this.professeurService.getByUserName(modules.getProfUsername());
		
		// Vérifie que le professeur existe
		if (!professeurs.isPresent()) {
			errors.rejectValue("professeur", "module.proffesseur.notvalid", "PROFESSEUR ERROR (ce professeur nexiste pas)");
			redirectionErreur(modules, rModel);
			return "module/addModule";
		}
		
		Module module = modules.toModules(cours.get(), professeurs.get());

		// Vérifie que les dates sont correct
		if (module.getDateDebut().isAfter(module.getDateFin()) || module.getDateDebut().isEqual(module.getDateFin())) {
			errors.rejectValue("dateDebut", "module.dateDebut.notvalid", "DATE ERROR(Date de debut doit etre avant la date de fin dans le calendrier)");
			redirectionErreur(modules, rModel);
			return "module/addModule";
		}

		// Vérifie que les dates sont correct
		if (module.getDateDebut().isBefore(LocalDate.now())) {
			errors.rejectValue("dateDebut", "module.dateDebut.notvalid", "DATE ERROR(Datte de debut doit etre apres la date d\'aujourdhui dans le calendrier)");
			redirectionErreur(modules, rModel);
			return "module/addModule";
		}
		
		// Vérifie que ce modules n'existe pas encore
		if (moduleService.exist(modules.getCode())) {
			errors.rejectValue("code", "module.code.doublon", "DUPLICATE ERROR (ce module exite deja)");
			redirectionErreur(modules, rModel);
			return "module/addModule";
		}

		moduleService.insert(module);
		log.info("Insertion du module <:> " + module);

		// redirection ver l'affichage de la liste des modules

		log.debug("redirection module/liste:");
		return "redirect:/module/liste";
	}

	/**
	 * Affiche le détail d'un module
	 * 
	 * @param code
	 * @param model
	 * @param locale
	 * @return
	 */
	@GetMapping("/{code}")
	public String detailModules(@PathVariable String code, Model model) {
		log.debug("Recherche le module: " + code);
		// si ce n'est pas une redirection, charge le module
		if (!model.containsAttribute("module"))
			// recherche le module dans la liste et ajout au model
			model.addAttribute("module",
					ModulesDto.toDto(moduleService.findOne(code).orElseThrow(() -> new NotExistException(code))));
		return "module/module";
	}
}
