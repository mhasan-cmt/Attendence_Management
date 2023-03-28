package org.isfce.pid.dao;

import java.util.List;
import java.util.Optional;

import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Module;
import org.isfce.pid.model.Module.MAS;
import org.isfce.pid.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IModuleJpaDao extends JpaRepository<Module, String> {
	// read
	//List<Module> readByCoursOrderByMomentAsc(Cours cours);
	List<Module> findByCoursOrderByMoment(Cours cours);
	
	// Optional<Module> findByCode(String code);
	// boolean existsByCode(String code);

	@Query(value = "select ?1||'-'||(count(*)+1)||'-A' from tmodule m where m.fkcours=?1", nativeQuery = true)
	String generateCodeModule(String code);

	//@Query("from TMODULE m join m.cours where m.moment=?2 and  ?1 member of m.cours.sections ")
	@Query("from TMODULE m join m.cours c join fetch m.inscriptions i where m.moment=?2 and ?1 member of c.sections")
	List<Module> getModulesSection(String string, MAS jour);
	
	@Query("from TMODULE m join fetch m.inscriptions i where m.professeur.user.username=?1 order by m.code")
	List<Module> getModuleProfesseur(String username);

	List<Module> findModuleByProfesseur_User_Username(String username);
	
	List<Module> findModuleByProfesseurOrderByCode(Professeur professeur);
	
	//inner join TINSCRIPTION i on i. FKMODULE=m.code
	//@Query("from TMODULE m join fetch m.inscriptions i where m.code=?1 order by m.code")
	//@Query("from TMODULE m where m.code=?1 order by m.code")
	//@Query("from TMODULE m inner join m.inscriptions i on i.FKMODULE=m.code where m.code=?1 order by m.code")
	//@Query("from TMODULE m inner join fetch m.inscriptions i on i.FKMODULE=m.code where m.code=?1 order by m.code")
	//Optional<Module> findModuleCode(String code);
	//@Query("SELECT m FROM TMODULE m LEFT JOIN m.inscriptions i WHERE m.code = ?1 ORDER BY m.code")
	//Optional<Module> findModuleCode(String code);
	@Query("SELECT m FROM TMODULE m INNER JOIN FETCH m.inscriptions i WHERE m.code = ?1 ORDER BY m.code")
	Optional<Module> findModuleCode(String code);
	//@Query("SELECT DISTINCT m FROM TMODULE m LEFT JOIN FETCH m.inscriptions i WHERE m.code = ?1 ORDER BY m.code")
	//Optional<Module> findModuleCode(String code);
	
	//@Query("SELECT m FROM TMODULE m WHERE m.code = ?1")
	//Optional<Module> findModuleCode(String code);
	
	@Modifying
	@Query("DELETE FROM TMODULE m WHERE m.code = ?1")
	void deleteModule(String code);
}
