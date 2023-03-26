package org.isfce.pid.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.isfce.pid.model.Module;
import org.isfce.pid.model.Presence;
import org.isfce.pid.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ISeanceJpaDao extends JpaRepository<Seance, Long>  {
	@Query("SELECT s FROM TSEANCE s WHERE s.module.code = ?1")
    List<Seance> findByModuleCode(String moduleCode);
	
	@Query("SELECT s FROM TSEANCE s WHERE s.id = ?1")
	Optional<Seance> findSeanceById(Long id);
	/*@Query("from TSEANCE m where m.module.code=?1")
	List<Seance> getSeanceFromModule(String module_code);
	
	@Query("from TSEANCE m where m.date=?2 and m.module.code=?1")
	List<Seance> getSeanceFromModule(String code, LocalDate date);
	
	@Query("select s from TSEANCE s join fetch s.presences where s.id = ?1")
	Seance getSeanceFromID(Long id);
	
	@Query("select s from TSEANCE s where s.id = ?1")
	Seance getSeanceByID(Long id);
	
	/*@Query("from TPRESENCE m join fetch m.seance join fetch m.etudiant where m.seance.id=?1")
	List<Presence> getPresenceFromSeance(Long idSeance);* /
	
	@Query("select m from TPRESENCE m join fetch m.seance join fetch m.etudiant where m.seance.id=?1")
	List<Presence> getPresenceFromSeance(Long idSeance);*/
}
