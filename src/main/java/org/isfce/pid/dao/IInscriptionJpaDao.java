package org.isfce.pid.dao;

import java.util.List;

import org.isfce.pid.model.Etudiant;
import org.isfce.pid.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IInscriptionJpaDao extends JpaRepository<Inscription, Long> {
	@Query("SELECT i FROM TINSCRIPTION i WHERE i.module.code = ?1 ORDER BY i.etudiant.user.username")
	List<Inscription> findInscriptionByModuleCode(String moduleCode);
	
	@Modifying
	@Query("DELETE FROM TINSCRIPTION i WHERE i.module.code = ?1")
	void deleteInscriptionsByModuleCode(String code);
}
