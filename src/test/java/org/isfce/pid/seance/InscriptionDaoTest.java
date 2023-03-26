package org.isfce.pid.seance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.isfce.pid.dao.IEtudiantJpaDao;
import org.isfce.pid.dao.IInscriptionJpaDao;
import org.isfce.pid.dao.IModuleJpaDao;
import org.isfce.pid.model.Etudiant;
import org.isfce.pid.model.Inscription;
import org.isfce.pid.model.Module;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles(value = "testU")
@Sql(scripts = { "/dataTestU.sql" }, config = @SqlConfig(encoding = "utf-8"))
@SpringBootTest
@Slf4j
public class InscriptionDaoTest {
	@Autowired
	private IInscriptionJpaDao inscriptionDao;
	
	@Autowired
	IModuleJpaDao moduleDao;

	@Transactional
	@Test
	public void testFindInscriptionByModuleCode() {
	    List<Inscription> inscriptions = inscriptionDao.findInscriptionByModuleCode("ISO2-1-A");
	
	    // Vérifier que la liste n'est pas vide
	    assertFalse(inscriptions.isEmpty());
	
	    // Vérifier que chaque inscription appartient au module avec le code donné
	    for (Inscription inscription : inscriptions) {
	        assertEquals("ISO2-1-A", inscription.getModule().getCode());
	    }
	
	    // Vérifier que la liste est triée par ordre alphabétique des noms d'utilisateur des étudiants
	    List<String> sortedUsernames = inscriptions.stream()
	            .map(i -> i.getEtudiant().getUser().getUsername())
	            .sorted()
	            .collect(Collectors.toList());
	    List<String> usernames = inscriptions.stream()
	            .map(i -> i.getEtudiant().getUser().getUsername())
	            .collect(Collectors.toList());
	    assertEquals(sortedUsernames, usernames);
	}
	
	@Transactional
	@Test
	public void testDeleteInscriptionByModuleCode() {
	    List<Inscription> inscriptions = inscriptionDao.findInscriptionByModuleCode("ISO2-1-A");
	
	    // Vérifier que la liste n'est pas vide
	    assertFalse(inscriptions.isEmpty());
	
	    // Vérifier que chaque inscription appartient au module avec le code donné
	    inscriptionDao.deleteInscriptionsByModuleCode("ISO2-1-A");
	    inscriptions = inscriptionDao.findInscriptionByModuleCode("ISO2-1-A");
		
	    // Vérifier que la liste n'est pas vide
	    assertTrue(inscriptions.isEmpty());
	}
}
