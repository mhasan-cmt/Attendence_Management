package org.isfce.pid.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="TPRESENCE")
public class Presence {
	public enum PresenceStatus{
		A, P, C, AJ
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	  
	private PresenceStatus status;
	  
	@ManyToOne
	private Etudiant etudiant;
	  
	@ManyToOne
	private Seance seance;
	
	private boolean etatCM;
}
