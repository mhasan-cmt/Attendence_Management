package org.isfce.pid.controller.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.isfce.pid.model.Cours;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursDto {
	@Pattern(regexp = "[A-Z]{2,8}[0-9]{0,3}", message = "{cours.codeID}")
	private String code;
	
	@NotBlank
	@Size(min=4, max=60, message="{elem.nom}")
	private String nom;
	
	@Min(value = 1, message = "{cours.nbPeriodes}")
	private short nbPeriodes;
	
	@NotEmpty(message = "{cours.sections.vide}")
	protected Set<String> sections = new HashSet<String>();

	/**
	 * Construction avec 3 attributs
	 * 
	 * @param code
	 * @param nom
	 * @param nbPeriodes
	 */
	public CoursDto(String code, String nom, short nbPeriodes) {
		this.code = code;
		this.nom = nom;
		this.nbPeriodes = nbPeriodes;
	}
	
	 public  Cours toCours() {
		 Cours cours=new Cours(this.code,this.nom,this.nbPeriodes);
		 cours.setSections(this.sections);
		 return cours;
	 }
	 
	 public static CoursDto toDto(Cours cours) {
		return new CoursDto(cours.getCode(),cours.getNom(),cours.getNbPeriodes(),
				cours.getSections());
	 }
}
