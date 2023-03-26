package org.isfce.pid.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


//@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "TINSCRIPTION")
public class Inscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Getter
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FKETUDIANT")
	//@Getter
	private Etudiant etudiant;

	@ManyToOne
	@JoinColumn(name = "FKMODULE")
	//@Getter
	private Module module;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
	
}


//@IdClass(InscriptionId.class)
/*@Id
@Column(name = "FKMODULE")
private String fkModule;

@Id
@Column(name = "FKETUDIANT")
private Integer fkEtudiant;

	@ManyToOne
	@JoinColumn(name = "FKMODULE", referencedColumnName = "CODE", insertable = false, updatable = false)
	private Module module;

	@ManyToOne
	@JoinColumn(name = "FKETUDIANT", referencedColumnName = "ID", insertable = false, updatable = false)
	private Etudiant etudiant;*/