package org.isfce.pid.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "TUSER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Data
public final class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 50, nullable = false)
	private final String username; // identifiant
	
	//password crypté
	@Column(length = 100, nullable = false)
	@ToString.Exclude
	private final String password;

	@Column(nullable = true)
	private final Roles role;
}
