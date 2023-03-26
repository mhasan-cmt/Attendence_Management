package org.isfce.pid.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class InscriptionId implements Serializable{
	private static final long serialVersionUID = 1L;

	  @Column(name = "FKMODULE")
	  private String fkModule;

	  @Column(name = "FKETUDIANT")
	  private Integer fkEtudiant;

}
