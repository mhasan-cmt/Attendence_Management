package org.isfce.pid.dao;

import java.util.List;
import java.util.Optional;

import org.isfce.pid.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProfesseurJpaDao extends JpaRepository<Professeur, Integer>{
	@Query("from TPROFESSEUR p join p.user u where u.username=?1")
	Optional<Professeur> findByUsername(String username);
		
	@Query("select u.username from TUSER u where u.role=1 order by u.username")
	List<String> getListProfUsername();
	
	@Query("select count(*)=1 from TUSER u where u.username=?1 and u.role=1")
	boolean existByUserName(String username);
	
	int countByNomAndPrenom(String nom, String prenom);
}
