package org.isfce.pid.dao;

import java.time.LocalDate;
import java.util.List;

import org.isfce.pid.model.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPresenceJpaDao  extends JpaRepository<Presence, Long>{
	
	@Query("from TPRESENCE m where m.seance.module.code=?1 and m.seance.date=?2 and m.etudiant.user.username=?3 order by m.etudiant.user.username")
	List<Presence> getPresenceFromEtudianAndSeance(String code, LocalDate date, String username);
	
	@Query("SELECT p FROM TPRESENCE p WHERE p.seance.id = ?1")
    List<Presence> findBySeanceId(Long seanceId);
}
