package org.isfce.pid.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.isfce.pid.dao.ISeanceJpaDao;
import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Presence;
import org.isfce.pid.model.Seance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SeanceServices {
	private ISeanceJpaDao seanceDao;
	
	//@Autowired
	//private SessionFactory sessionFactory;

	public SeanceServices(ISeanceJpaDao seance) {
		// TODO Auto-generated constructor stub
		this.seanceDao = seance;
	}
	
	public List<Seance> findByModuleCode(String moduleCode)
	{
		return seanceDao.findByModuleCode(moduleCode);
	}
	
	public Optional<Seance> findSeanceById(Long id)
	{
		return seanceDao.findSeanceById( id);
	}
	
	/**
	 * Ajout d'une nouvelle seance
	 * 
	 * @param seance
	 * @return
	 */
	public Seance insert(Seance seance) {
		assert seance != null : "La seance doit exister";
		return seanceDao.save(seance);
	}
}
