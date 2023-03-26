package org.isfce.pid.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "TSECRETAIRE")
//Rajout d'une contrainte d'unicité sur la table
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nom", "prenom" }))
public class Secretaire {
	@Id // Id généré par la base de données
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter //pas de setter
	private Integer id;

	@Getter
	@OneToOne(optional = false, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	@JoinColumn(name = "FKUSER", unique = true, nullable = false, updatable = false)
	private User user;

	@NotNull
	@Size(min = 1, max = 40, message = "{elem.nom}")
	@Column(length = 40, nullable = false)
	private String nom;
	
	@NotNull
	@Size(min = 1, max = 40, message = "{elem.prenom}")
	@Column(length = 40, nullable = false)
	private String prenom;

	@Email(message = "{email.nonValide}")
	@NotNull
	@Column(length = 50, nullable = false)
	private String email;

	/**
	 * @param nom
	 * @param prenom
	 * @param email
	 * @param user
	 */
	public Secretaire(String nom, String prenom, String email, User user) {
		this(null, nom, prenom, email, user);
	}

	/**
	 * @param id
	 * @param user
	 * @param nom
	 * @param prenom
	 * @param email
	 */
	public Secretaire(Integer id, String nom, String prenom, String email, User user) {
		assert (user.getRole() == Roles.ROLE_SECRETARIAT);
		this.id = id;
		this.user = user;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		//this.user.setRole(Roles.ROLE_PROF);
	}
}
