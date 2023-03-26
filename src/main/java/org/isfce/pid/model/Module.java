package org.isfce.pid.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.isfce.pid.util.validation.annotation.DatesPastAndFutureValidation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

//@Data
//@EqualsAndHashCode.Exclude
//@ToString.Exclude
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "TMODULE")
@DatesPastAndFutureValidation(d1 = "dateDebut", d2 = "dateFin", message = "{date.compare}")
public class Module {
	public static enum MAS {
		MATIN, APM, SOIR;
	}

	@Id
	@Pattern(regexp = "[A-Z0-9]{3,8}-[0-9]-[A-Z]", message = "Module : {elem.code}")
	@Column(length = 12)
	private String code;

	@NotNull
	@Column(nullable = false)
	private LocalDate dateDebut;

	@NotNull
	@Column(nullable = false)
	private LocalDate dateFin;

	@NotNull
	@Column(nullable = false)
	private MAS moment;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "FKCOURS", nullable = false)
	@NotNull
	private Cours cours;

	@ManyToOne
	@JoinColumn(name = "FKPROFESSEUR")
	private Professeur professeur;
	
	@OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
	private Set<Inscription> inscriptions = new HashSet<>();
	
	public void addInscription(Inscription inscription) {
		inscriptions.add(inscription);
		inscription.setModule(this);
	}
	
	public void removeInscription(Inscription inscription) {
		inscriptions.remove(inscription);
		inscription.setModule(null);
	}
	
	public Set<Etudiant> getEtudiants() {
		Set<Etudiant> etudiants = new HashSet<>();
		for (Inscription inscription : inscriptions) {
			etudiants.add(inscription.getEtudiant());
		}
		return etudiants;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	public MAS getMoment() {
		return moment;
	}

	public void setMoment(MAS moment) {
		this.moment = moment;
	}

	public Cours getCours() {
		return cours;
	}

	public void setCours(Cours cours) {
		this.cours = cours;
	}

	public Professeur getProfesseur() {
		return professeur;
	}

	public void setProfesseur(Professeur professeur) {
		this.professeur = professeur;
	}

	public Set<Inscription> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(Set<Inscription> inscriptions) {
		this.inscriptions = inscriptions;
	}
}
