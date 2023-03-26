package org.isfce.pid.service;


import org.isfce.pid.dao.ISecretaireJpaDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SecretaireServices {
	private ISecretaireJpaDao secretaireDao;

	public SecretaireServices(ISecretaireJpaDao secretaire) {
		// TODO Auto-generated constructor stub
		this.secretaireDao = secretaire;
	}
}