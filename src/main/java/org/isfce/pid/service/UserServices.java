package org.isfce.pid.service;

import java.util.Optional;

import javax.security.auth.login.CredentialException;

import org.isfce.pid.controller.exceptions.NotExistException;
import org.isfce.pid.dao.IUserJpaDao;
import org.isfce.pid.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@Service
public class UserServices {
	@Autowired
	PasswordEncoder encodeur;
	
	@Autowired
	private IUserJpaDao userDao;

	
	public Optional<User> findById(String username) throws UsernameNotFoundException {
		return userDao.findById(username);
	}

	/**
	 * Un utilisateur est généralement lié à une personne
	 * sauf dans des cas particuliers comme pour l'administrateur
	 * 
	 * @param user
	 * @return le user sauvé qui peut avoir été modifié suite à la persistance
	 */
	public User createUser(User user) {
		return userDao.save(user);
	}

	/**
	 * Supprime l'utilisateur 
	 * cependant il ne devra plus être lié à une personne (etudiant,...)
	 * 
	 * @param username
	 */
	public void deleteUser(String username) {
		userDao.deleteById(username);
	}

	/**
	 * 
	 * @param user 
	 * @param oldPassword non crypté
	 * @param newPassword non crypté et supposé validé
	 * @throws CredentialException 
	 */
	@PreAuthorize("hasRole('ADMIN') or  #user.username eq authentication.name")
	public void changePassword(User user, String oldPassword, String newPassword) throws CredentialException {
		//vérifie l'existance du user
		//récupère le user dans la BD pour voir s'il existe bien 
		User userDb=userDao.findById(user.getUsername()).orElseThrow(
				()->new NotExistException("{user.inexistant}", user.getUsername()));
		//Vérifie qu'il correspond avec celui à modifier
		if(! user.equals(userDb))
			throw  new CredentialException("{user.different}");
		
		//vérification du pw
		if(!encodeur.matches(oldPassword, userDb.getPassword()))
			throw new CredentialException("{user.badPassword}");	
		userDao.save(new User(user.getUsername(),encodeur.encode(newPassword),user.getRole()));
	}

	public boolean existsById(String username) {
		return userDao.existsById(username);
	}

}
