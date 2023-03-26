package org.isfce.pid.dao;

import java.util.List;
import java.util.Optional;

import org.isfce.pid.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEtudiantJpaDao extends JpaRepository<Etudiant, Integer> {
	@Query("from TETUDIANT p join p.user u where u.username=?1")
	Optional<Etudiant> findByUsername(String username);
	
	@Query("from TETUDIANT p join p.user u where p.id=?1")
	Optional<Etudiant> findById(Integer id);
		
	@Query("select u.username from TUSER u where u.role=2 order by u.username")
	List<String> getListEtudiantUsername();
	
	@Query("select count(*)=1 from TUSER u where u.username=?1 and u.role=2")
	boolean existByUserName(String username);
	
	int countByNomAndPrenom(String nom, String prenom);
}
