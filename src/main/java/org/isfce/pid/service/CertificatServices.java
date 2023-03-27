package org.isfce.pid.service;

import org.isfce.pid.dao.ICertificatJpaDao;
import org.isfce.pid.model.Certificat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@Service
public class CertificatServices {
	private ICertificatJpaDao certificatDao;

	public CertificatServices(ICertificatJpaDao certificat) {
		// TODO Auto-generated constructor stub
		this.certificatDao = certificat;
	}
	
	public Certificat insert(Certificat certificat) {
		assert certificat != null : "Le certificat doit exister";
		return certificatDao.save(certificat);
	}

	public Optional<Certificat> findByEtudiantId(Integer id) {
		return certificatDao.findByEtudiantId(id);
	}
}