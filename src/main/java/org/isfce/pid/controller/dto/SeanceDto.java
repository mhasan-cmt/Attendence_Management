package org.isfce.pid.controller.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Module;
import org.isfce.pid.model.Presence;
import org.isfce.pid.model.Professeur;
import org.isfce.pid.model.Seance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeanceDto {
	@NotNull
    private String module;

    @NotNull
	private LocalDate date;
    
    @NotNull
	private boolean cloturer;
    
    public  Seance toSeance(Module module) {
		 Seance seance = new Seance(null, module, this.date, this.cloturer);
		 return seance;
	 }
	 
	 public static SeanceDto toDto(Seance seance) {
		return new SeanceDto(seance.getModule().getCode(), seance.getDate(), seance.isCloturer());
	 }
}
