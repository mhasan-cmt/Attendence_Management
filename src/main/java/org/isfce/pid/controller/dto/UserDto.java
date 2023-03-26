package org.isfce.pid.controller.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.isfce.pid.model.Roles;
import org.isfce.pid.model.User;
import org.isfce.pid.util.validation.annotation.PasswordValueMatch;
import org.isfce.pid.util.validation.annotation.ValidPassword;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;

@PasswordValueMatch.List({
		@PasswordValueMatch(groups = CredentialValidation.class, 
							field = "password", fieldMatch = "confirmPassword", 
							message = "{PasswordMatchError}") })
@Data
@AllArgsConstructor
public class UserDto {

	@NotBlank(groups = CredentialValidation.class)
	private String username;

	@ValidPassword(groups = CredentialValidation.class)
	private String password;

	private String confirmPassword;

	@NotNull(groups = CredentialValidation.class)
	private Roles role;

	@Valid
	public UserDto() {
		username = "";
		password = "";
		confirmPassword = "";
	}

	/**
	 * Créer un user à partir d'un Dto sans crypter le password
	 * 
	 * @param encodeur
	 * @return User
	 */
	public User toUser() {
		return new User(username, password, role);
	}

	
	public User toUser(PasswordEncoder encodeur) {
		return new User(username, encodeur.encode(password), role);
	}

	
	public static UserDto toUserDto(User user) {
		return new UserDto(user.getUsername(), user.getPassword(), user.getPassword(), user.getRole());
	}
}
