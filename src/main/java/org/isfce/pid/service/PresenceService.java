package org.isfce.pid.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.isfce.pid.controller.SeanceController;
import org.isfce.pid.dao.IPresenceJpaDao;
import org.isfce.pid.model.Presence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class PresenceService {
	private IPresenceJpaDao presenceDao;

	public PresenceService(IPresenceJpaDao presenceDao) {
		// TODO Auto-generated constructor stub
		this.presenceDao = presenceDao;
	}
	
	public List<Presence> getPresenceFromEtudianAndSeance(String code, LocalDate date, String username)
	{
		return presenceDao.getPresenceFromEtudianAndSeance(code, date, username);
	}
	
	public List<Presence> getAll()
	{
		return presenceDao.findAll();
	}
	
	/**
	 * Ajout d'une nouvelle seance
	 * 
	 * @param seance
	 * @return
	 */
	public Presence insert(Presence presence) {
		assert presence != null : "La presence doit exister";
		log.debug("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + presence);
		return presenceDao.save(presence);
	}
	
	/**
	 * Ajout d'une nouvelle seance
	 * 
	 * @param seance
	 * @return
	 */
	public Presence update(Presence presence) {
		assert presence != null : "La presence doit exister";
		log.debug("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + presence);
		return presenceDao.save(presence);
	}
	
	public List<Presence> findBySeanceId(Long seanceId)
	{
		return presenceDao.findBySeanceId(seanceId);
	}
	
	public Optional<Presence> findById(Long id) {
		return presenceDao.findById(id);
	}
}
