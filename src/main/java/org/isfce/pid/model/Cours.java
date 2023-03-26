package org.isfce.pid.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

//import org.isfce.pid.util.validation.annotation.Min;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="TCOURS")
public class Cours {
	@Pattern(regexp = "[A-Z]{2,8}[0-9]{0,3}", message = "{cours.codeID}")
	@Id  //signifie que l'identifiant (généré manuellement par l'application)
	@Column(length=11) //limite à 11 caractères
	private String code;
	
	@NotBlank
	@Size(min=4, max=60, message="{elem.nom}")
	@Column(length=60, nullable = false) //taille et not null
	private String nom;
	
	@Min(value=1, message="{cours.nbPeriodes}")
	@Column
	private short nbPeriodes;
	
	@NotEmpty(message = "{cours.sections.vide}")
	@ElementCollection(fetch = FetchType.EAGER) //mappage d'une collection d'un type élémentaire
	@CollectionTable( name = "TSECTION", // nom de la table
			joinColumns = @JoinColumn(name = "FKCOURS")) // nom de la clé étrangère
	@Column(name = "SECTION", length = 30) // nom du champ (par defaut ce serait: sections)
	protected Set<String> sections = new HashSet<String>();

	/**
	 * Construction avec 3 attributs
	 * 
	 * @param code
	 * @param nom
	 * @param nbPeriodes
	 */
	public Cours(String code, String nom, short nbPeriodes) {
		this.code = code;
		this.nom = nom;
		this.nbPeriodes = nbPeriodes;
	}
	
	//ma propre méthode pour ajouter une section
	public void addSection(String section) {
		sections.add(section);
	}
	
	public void removeSection(String section) {
		sections.remove(section);
	}

}
