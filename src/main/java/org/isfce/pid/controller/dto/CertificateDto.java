package org.isfce.pid.controller.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.isfce.pid.model.Certificat;
import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Etudiant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDto {
	@NotNull
	private String id;
	
	@NotBlank
	private String nom;
	
	@NotBlank
	private String prenom;
	
	@NotNull
	private LocalDate dateDebut;

	@NotNull
	private LocalDate dateFin;

	@NotNull
    private String nomDocteur;
	
	
	 public  Certificat toCertificate(Etudiant e) {
		 Certificat certificat = new Certificat(null, e, this.dateDebut, this.dateFin, this.getNomDocteur());
		 return certificat;
	 }
	 
	 public static CertificateDto toDto(Certificat certificat) {
		return new CertificateDto("" + certificat.getEtudiant().getId(), certificat.getEtudiant().getNom(),
				certificat.getEtudiant().getPrenom(), certificat.getDateDebut(), certificat.getDateFin(), certificat.getNomDocteur());
	 }
}
