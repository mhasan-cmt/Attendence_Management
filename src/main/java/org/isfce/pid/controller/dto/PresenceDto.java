package org.isfce.pid.controller.dto;

import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.isfce.pid.model.Etudiant;
import org.isfce.pid.model.Module;
import org.isfce.pid.model.Presence;
import org.isfce.pid.model.Seance;
import org.isfce.pid.model.Presence.PresenceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresenceDto {
	@NotNull
	private PresenceStatus status;
	
	@NotNull
	private boolean nature;
	
	public  Presence toSeance(Long id, Etudiant etudiant, Seance seance) {
		Presence presence = new Presence(id, this.status, etudiant, seance, this.nature);
		 return presence;
	 }
	 
	 public static PresenceDto toDto(Presence presence) {
		return new PresenceDto(presence.getStatus(), presence.isEtatCM());
	 }
}
