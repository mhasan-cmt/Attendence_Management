package org.isfce.pid.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.isfce.pid.util.validation.annotation.DatesPastAndFutureValidation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DatesPastAndFutureValidation(d1 = "dateDebut", d2 = "dateFin", message = "{date.compare}")
@Entity(name="TCERTIFICAT")
public class Certificat {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FKETUDIANT")
    private Etudiant etudiant;

    @NotNull
	@Column(nullable = false)
	private LocalDate dateDebut;

	@NotNull
	@Column(nullable = false)
	private LocalDate dateFin;

	@NotNull
	@Column(nullable = false)
    private String nomDocteur;

}

