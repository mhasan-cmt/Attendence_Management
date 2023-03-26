package org.isfce.pid.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.isfce.pid.dto.PresenceWrapperDto;
import org.isfce.pid.service.CoursServices;
import org.isfce.pid.service.EtudiantService;
import org.isfce.pid.service.ModuleServices;
import org.isfce.pid.service.PresenceService;
import org.isfce.pid.service.ProfesseurServices;
import org.isfce.pid.service.SeanceServices;
import org.isfce.pid.util.validation.LocalDateEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.hibernate.SessionFactory;
import org.isfce.pid.controller.dto.CoursDto;
import org.isfce.pid.controller.dto.PresenceDto;
import org.isfce.pid.controller.dto.SeanceDto;
import org.isfce.pid.model.Etudiant;
import org.isfce.pid.model.Inscription;
import org.isfce.pid.model.Module;
import org.isfce.pid.model.Seance;
import org.isfce.pid.model.Module.MAS;
import org.isfce.pid.model.Presence;
import org.isfce.pid.model.Presence.PresenceStatus;
import org.isfce.pid.model.PresenceWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/seance")
public class SeanceController {
    @Autowired
    private SeanceServices seanceService;
	  
	/*@Autowired 
	private CoursServices coursService;
	  
	@Autowired
	private EtudiantService etudiantService;*/

    @Autowired
    private ModuleServices moduleService;

    //@Autowired
    //private ProfesseurServices professeurService;

    @Autowired
    private PresenceService presenceService;

    int i = 0;


    @Autowired
    public SeanceController(SeanceServices seanceService, ModuleServices moduleService, PresenceService presenceService) {
        this.seanceService = seanceService;
        //this.coursService = coursService;
        //this.etudiantService = etudiantService;
        this.moduleService = moduleService;
        //this.professeurService = professeurService;
        this.presenceService = presenceService;
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
    @Transactional
    @GetMapping("/liste/{username}")
    public String listeModules(@PathVariable String username, Model model) {
        List<Module> modules = moduleService.getModulesProf(username);
        log.debug("*********************taille : " + modules.size());
        model.addAttribute("modulesList", modules);
        return "seance/listeModules";
    }

    /**
     * Liste des modules
     *
     * @param model
     * @return la nom logique de la vue qui affichera la liste des modules
     */
    @GetMapping("/liste_seance/{code}")
    public String listeSeance(@PathVariable String code, Model model) {
        List<Seance> seances = seanceService.findByModuleCode(code);
        log.debug("*********************taille : " + seances.size());
        model.addAttribute("seancesList", seances);
        model.addAttribute("code", code);
        return "seance/listeSeances";
    }

    /**
     * Methode Get pour ajouter un modules
     *
     * @param modules un modules avec des valeurs par défaut
     * @return nom logique de la vue pour créer un modules
     */
    @GetMapping("add/{code}")
    public String addSeanceGet(@PathVariable String code, @ModelAttribute("seance") SeanceDto seance, Model model) {
        log.debug("affiche la vue pour ajouter un module ");

        Optional<Module> module = moduleService.findOne(code);

        if (!module.isPresent()) {
            log.debug("Module Inexistant");
            return "redirect:/error";
        }

        //SeanceDto seance = new SeanceDto(module.get(), LocalDate.now(), false);
        seance.setDate(LocalDate.now());
        seance.setModule(module.get().getCode());

        log.debug("/////////////////////////// " + seance);

        //model.addAttribute("seance", seance);
        return "seance/addSeance";
    }

    /**
     * Méthode Post pour créer le nouveau modules
     *
     * @param modules le modules
     * @param errors  les erreurs de validation
     * @param rModel  une map qui peut survivre à une redirection
     * @return une URL de redirection
     */
    @PostMapping("add/{code}")
    public String addSeancePost(@PathVariable String code, @Valid @ModelAttribute("seance") SeanceDto seance, BindingResult errors,
                                Model rModel, RedirectAttributes model) {
        log.info("POST d'un module " + seance);

        // Gestion de la validation en fonction des annotations
        if (errors.hasErrors()) {
            log.debug("Erreurs dans les données de la seance:" + seance.getModule() + "; date:" + seance.getDate());
            rModel.addAttribute("seance", seance);
            return "seance/addSeance";
        }

        // Vérifie que les dates sont correct
        if (seance.getDate().isBefore(LocalDate.now())) {
            errors.rejectValue("date", "seance.date.notvalid", "DATE ERROR(Date de debut doit etre apres la date d\'aujourdhui dans le calendrier)");
            rModel.addAttribute("seance", seance);
            return "seance/addSeance";
        }

        List<Seance> seances = seanceService.findByModuleCode(code);

        // Vérifie que cette seance n'existe pas encore
        if (seances.size() > 0) {
            for (Seance s : seances) {
                if (s.getDate().isAfter(seance.getDate())) {
                    errors.rejectValue("date", "seance.date.doublon", "DUPLICATE ERROR (cette seance exite deja)");
                    rModel.addAttribute("seance", seance);
                    return "seance/addSeance";
                }
            }
        }

        Optional<Module> module = moduleService.findByCode(code);

        if (!module.isPresent() || seance.getModule() == null || !code.equals(seance.getModule())) {
            errors.rejectValue("module", "seance.module.code.invalid", "MODULE ERROR (ce module n'existe pas)");
            rModel.addAttribute("seance", seance);
            return "seance/addSeance";
        }

        Seance s = seanceService.insert(seance.toSeance(module.get()));
        log.info("Insertion de la seance <:> " + seance);

        for (Etudiant etudiant : module.get().getEtudiants()) {
            Presence presence = new Presence(null, PresenceStatus.A, etudiant, s, false);
            log.info("Insertion de la presence debut <:> " + seance);
            presence = presenceService.insert(presence);
            log.info("Insertion de la presence fin <:> " + seance);
        }

        model.addFlashAttribute("seance", s);

        log.debug("redirection seance/presence:");
        return "redirect:/seance/presence/" + s.getId();
    }

    /**
     * Liste des modules
     *
     * @param model
     * @return la nom logique de la vue qui affichera la liste des modules
     */
    @GetMapping("/presence/{id}")
    public String presenceGet(@PathVariable Long id, Model model) {
        i++;
        List<Presence> presences = presenceService.findBySeanceId(id);


        Map<String, PresenceStatus> status = new HashMap<>();
        status.put("ABSCENT", PresenceStatus.A);
        status.put("PRESENT", PresenceStatus.P);
        status.put("CONNECTE", PresenceStatus.C);
        status.put("ABSCENCE JUSTIFIER", PresenceStatus.AJ);

//        log.debug("*********************taille presences : " + presenceWrapperDto.getPresences().size());
        log.debug("*********************taille presences+ : " + presences.size());
        //log.debug("*********************i+ : " + i + ", presence = " + presence);
        PresenceWrapperDto presenceWrapperDto = new PresenceWrapperDto();
        Seance seance = presences.get(0).getSeance();
        presenceWrapperDto.setPresences(presences);
        //model.addAttribute("presence", presence);
        model.addAttribute("seance", seance);
        model.addAttribute("presencesWrapper", presenceWrapperDto);
        model.addAttribute("statusList", status);
        model.addAttribute("module_code", seance.getModule().getCode());
        model.addAttribute("seance_date", seance.getDate());
        return "seance/listePresence";
    }

    @PostMapping("/updatePresence")
    public String updatePresencePost(@ModelAttribute PresenceWrapperDto presenceWrapperDto, /*BindingResult errors, */Model model) {
        presenceWrapperDto.getPresences().forEach(presence -> {
            Optional<Presence> presence_ = presenceService.findById(presence.getId());
            if (presence_.isPresent()) {
                log.debug("*********************Avant mise a jour : " + presence_.get());
                presence_.get().setStatus(presence.getStatus());
                presenceService.update(presence_.get());
                log.debug("*********************Apres mise a jour : " + presence);
            }
        });
        Seance seance = presenceWrapperDto.getPresences().get(0).getSeance();
        return presenceGet(seance.getId(), model);
    }

    @PostMapping("/cloturer")
    public String cloturerSession(@RequestParam Long ids, RedirectAttributes redirectAttributes) {
        Optional<Seance> seance = seanceService.findSeanceById(ids);

        if (!seance.isPresent()) {
            log.debug("Valeur incorrect");
            return "redirect:/error";
        }
        seance.get().setCloturer(true);
        seanceService.insert(seance.get());
        return "redirect:/seance/presence/" + seance.get().getId();
    }
}
