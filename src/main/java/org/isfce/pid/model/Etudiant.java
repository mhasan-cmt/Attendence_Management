package org.isfce.pid.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.isfce.pid.model.Module.MAS;
import org.isfce.pid.util.validation.annotation.DatesPastAndFutureValidation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "TETUDIANT")
//Rajout d'une contrainte d'unicité sur la table
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nom", "prenom" }))
public class Etudiant {
	@Id // Id généré par la base de données
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Getter //pas de setter
	private Integer id;

	//@Getter
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
	
	@NotNull
    @Size(min = 10, max = 10, message = "{elem.tel}")
    @Column(length = 10, nullable = false)
    private String tel;

	@OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Presence> presences = new HashSet<>();

	/**
	 * @param nom
	 * @param prenom
	 * @param email
	 * @param user
	 * @param tel
	 */
	public Etudiant(String nom, String prenom, String email, String tel, User user) {
		this(null, nom, prenom, email, tel, user);
	}

	/**
	 * @param id
	 * @param user
	 * @param nom
	 * @param prenom
	 * @param email
	 * @param tel
	 */
	public Etudiant(Integer id, String nom, String prenom, String email, String tel, User user) {
		assert (user.getRole() == Roles.ROLE_ETUDIANT);
		this.id = id;
		this.user = user;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.tel =  tel;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Set<Presence> getPresences() {
		return presences;
	}

	public void setPresences(Set<Presence> presences) {
		this.presences = presences;
	}

	public Integer getId() {
		return this.id;
	}

	public User getUser() {
		return this.user;
	}
}
