package org.isfce.pid.controller;

import org.isfce.pid.model.Presence;
import org.isfce.pid.service.CertificatServices;
import org.isfce.pid.service.EtudiantService;
import org.isfce.pid.service.ModuleServices;
import org.isfce.pid.service.PresenceService;
import org.isfce.pid.service.SeanceServices;
import org.isfce.pid.util.validation.LocalDateEditor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.isfce.pid.controller.dto.CertificateDto;
import org.isfce.pid.controller.dto.SeanceDto;
import org.isfce.pid.model.Certificat;
import org.isfce.pid.model.Etudiant;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/certificat")
public class CertificateController {
	@Autowired
    private EtudiantService etudiantService;

    @Autowired
    private CertificatServices certificateService;
    
    @Autowired
    private SeanceServices seanceService;
    
    @Autowired
    private ModuleServices moduleService;
    
    @Autowired
	public CertificateController(CertificatServices certificateService, EtudiantService etudiantService, SeanceServices seanceService, ModuleServices moduleService) {
		this.certificateService = certificateService;
		this.etudiantService = etudiantService;
		this.seanceService = seanceService;
		this.moduleService = moduleService;
	}
    
    @InitBinder
	public void bindingPreparation(WebDataBinder binder) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(formatter));
	}

    @GetMapping("/add")
    public String nouveauCertificateGet(@ModelAttribute("certificat") CertificateDto certificat, Model model) {
    	certificat.setId("");
        return "certificat/addCertificat";
    }

    @PostMapping("/add")
    public String nouveauCertificatePost(@RequestParam(required = false) String action, @ModelAttribute("certificat") CertificateDto certificat, BindingResult errors, Model model) {
    	if (action != null && action.equals("recherche")) {
    		if (certificat.getId() == null)
        	{
        		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (vous devez fournir un id)<<" + certificat + ">>");
        		return "certificat/addCertificat";
        	}
        	
        	Integer id;
        	try {
        	    id = Integer.valueOf(certificat.getId());
        	} catch (NumberFormatException e) {
        		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (un identifiant doit etre un entier) <<" + certificat.getId() + ">>");
        		return "certificat/addCertificat";
        	}
        	Optional<Etudiant> etudiant = etudiantService.findById(id);
        	
        	if (!etudiant.isPresent()) {
        		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (cet etudiant n\'existe pas)");
        		return "certificat/addCertificat";
        	}
        	
        	certificat.setId(certificat.getId());
        	certificat.setNom(etudiant.get().getNom());
        	certificat.setPrenom(etudiant.get().getPrenom());
        	
        	return "certificat/addCertificat";
    	} else if (action != null &&  action.equals("add")) {
    		if (errors.hasErrors()) {
    			return "certificat/addCertificat";
    		}
        	
        	Integer id;
        	try {
        	    id = Integer.valueOf(certificat.getId());
        	} catch (NumberFormatException e) {
        		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (un identifiant doit etre un entier) <<" + certificat + ">>");
        		return "certificat/addCertificat";
        	}
        	Optional<Etudiant> etudiant = etudiantService.findById(id);
        	
        	if (!etudiant.isPresent()) {
        		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (cet etudiant n\'existe pas)");
        		return "certificat/addCertificat";
        	}
        	
        	// Vérifie que les dates sont correct
        	if (certificat.getDateDebut().isAfter(certificat.getDateFin())) {
        		errors.rejectValue("dateDebut", "certificatdto.dateDebut.notvalid", "DATE ERROR(Date de debut doit etre avant la date de fin dans le calendrier)");
        		return "certificat/addCertificat";
        	}

        	// Vérifie que les dates sont correct
        	if (certificat.getDateDebut().isBefore(LocalDate.now())) {
        		errors.rejectValue("dateDebut", "certificatdto.dateDebut.notvalid", "DATE ERROR(Datte de debut doit etre apres la date d\'aujourdhui dans le calendrier)");
        		return "certificat/addCertificat";
        	}
        	
        	if (certificat.getNomDocteur().isBlank())
        	{
        		errors.rejectValue("nomDocteur", "certificatdto.nomDocteur.notvalid", "NOM DOCTEUR ERROR(le nom du docteur ne peut ni etre vide ni contenir uniquement des espaces)");
        		return "certificat/addCertificat";
        	}
			if(etudiant.get().getPresences()!=null){
        	certificateService.insert(certificat.toCertificate(etudiant.get()));
			if (etudiant.get().getPresences()!=null){
				etudiant.get().getPresences().forEach(presence -> {
					presence.setEtatCM(true);
					presence.setStatus(Presence.PresenceStatus.AJ);
				});
			}
			etudiantService.save(etudiant.get());
			}

            return "redirect:/";
    	}
    	return "certificat/addCertificat";
    }
    
    @PostMapping("/recherche")
    public String chercherEtudiant(@RequestParam(required = false) String etudiantId, @ModelAttribute("certificat") CertificateDto certificat, BindingResult errors, Model model) {
    	certificat.setId("");
    	certificat.setNom("");
    	certificat.setPrenom("");
    	if (etudiantId == null)
    	{
    		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (vous devez fournir un id)");
    		return "certificat/addCertificat";
    	}
    	
    	Integer id;
    	try {
    	    id = Integer.valueOf(etudiantId);
    	} catch (NumberFormatException e) {
    		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (un identifiant doit etre un entier) <<" + etudiantId + ">>");
    		return "certificat/addCertificat";
    	}
    	Optional<Etudiant> etudiant = etudiantService.findById(id);
    	
    	if (!etudiant.isPresent()) {
    		errors.rejectValue("id", "certificatdto.id.ivalide", "ID ERROR (cet etudiant n\'existe pas)");
    		return "certificat/addCertificat";
    	}
    	
    	certificat.setId(etudiantId);
    	certificat.setNom(etudiant.get().getNom());
    	certificat.setPrenom(etudiant.get().getPrenom());
    	
    	return "certificat/addCertificat";
    	//return "redirect:/certificat/add";
    }
}
