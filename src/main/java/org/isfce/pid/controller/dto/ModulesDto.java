package org.isfce.pid.controller.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Professeur;
import org.isfce.pid.model.Module.MAS;
import org.isfce.pid.model.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModulesDto {
	@Id
	@Pattern(regexp = "[A-Z0-9]{3,8}-[0-9]-[A-Z]", message = "Modules: {elem.code}")
	private String code;

	@NotNull
	private LocalDate dateDebut;

	@NotNull
	private LocalDate dateFin;

	@NotNull
	private MAS moment;

	@NotNull
	private String coursCode;

	@NotNull
	private String profUsername;
	
	 public  Module toModules(Cours cour, Professeur prof) {
		 Module module = new Module(this.code, this.dateDebut , this.dateFin, this.moment, cour, prof, null);
		 return module;
	 }
	 
	 public static ModulesDto toDto(Module module) {
		return new ModulesDto(module.getCode(), module.getDateDebut(),
				module.getDateFin(), module.getMoment()
				, module.getCours().getCode(), module.getProfesseur().getUser().getUsername());
	 }
}
