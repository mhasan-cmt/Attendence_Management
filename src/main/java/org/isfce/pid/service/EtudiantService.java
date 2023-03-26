package org.isfce.pid.service;

import java.util.Optional;

import org.isfce.pid.dao.IEtudiantJpaDao;
import org.isfce.pid.model.Etudiant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EtudiantService {
	private IEtudiantJpaDao etudiantDao;

	public EtudiantService(IEtudiantJpaDao etudiant) {
		// TODO Auto-generated constructor stub
		this.etudiantDao = etudiant;
	}
	
	public Optional<Etudiant> findById(Integer id)
	{
		return etudiantDao.findById(id);
	}
	public void save(Etudiant etudiant)
	{
		etudiantDao.save(etudiant);
	}
}
