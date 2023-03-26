package org.isfce.pid.module;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.transaction.Transactional;

import org.isfce.pid.dao.IEtudiantJpaDao;
import org.isfce.pid.dao.IModuleJpaDao;
import org.isfce.pid.dao.IProfesseurJpaDao;
import org.isfce.pid.dao.IUserJpaDao;
import org.isfce.pid.dao.IInscriptionJpaDao;
import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Etudiant;
import org.isfce.pid.model.Module;
import org.isfce.pid.model.Professeur;
import org.isfce.pid.model.Module.MAS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles(value = "testU")
@Sql(scripts = { "/dataTestU.sql" }, config = @SqlConfig(encoding = "utf-8"))
@SpringBootTest
@Slf4j
public class TestDaoModule {
	Cours geb = new Cours ("IGEB", "Gestion de base de données", ( short ) 60) ;
	
	@Autowired
	IProfesseurJpaDao professeurDao;
	@Autowired
	IModuleJpaDao moduleDao;
	@Autowired
	IEtudiantJpaDao etudiantDao;
	@Autowired
	IInscriptionJpaDao inscriptionDao;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Transactional
	@Test
	public void testGetModuleForCours() {
		geb.addSection("Informatique");
		//geb.getSections().add("Informatique");
		// veriei le APM et le SOIR
		List<Module> modules = moduleDao.findByCoursOrderByMoment(geb);
		assertThat(modules.size()).isEqualTo(2);
		assertThat(modules.get(0).getMoment()).isEqualTo(MAS.APM);
		assertThat(modules.get(1).getMoment()).isEqualTo(MAS.SOIR);
	}

	@Transactional
	@Test
	public void testGenerateCodeModule() {
		String code = moduleDao.generateCodeModule("IGEB");
		assertThat(code).isEqualTo("IGEB-3-A");
	}

	@Transactional
	@Test
	public void testGetModulesForSectionAndTime() {
		List<Module> modules = moduleDao.getModulesSection("Informatique", MAS.SOIR);
		assertThat(modules.size()).isEqualTo(5);
	}

	@Transactional
	@Test
	public void testGetModulesForProfesseurId() {
		Professeur professeur = professeurDao.findById(1).orElse(null);
		assertTrue(professeur != null);
		List<Module> modules = moduleDao.findModuleByProfesseurOrderByCode(professeur);
		assertThat(modules.size()).isEqualTo(6);
	}
	
	@Transactional
	@Test
	public void testGetModulesForProfesseurUsername() {
		List<Module> modules = moduleDao.getModuleProfesseur("vo");
		assertThat(modules.size()).isEqualTo(6);
	}
	
	@Transactional
	@Test
	public void testInscription() {
		List<Module> modules = moduleDao.getModuleProfesseur("vo");
        List<Etudiant> etudiants = etudiantDao.findAll();
        
        if (modules.size() > 0 && etudiants.size() > 0) {
            Module unModule = modules.get(0);
            Etudiant unEtudiant = etudiants.get(etudiants.size() - 1);
            long count = unModule.getEtudiants().size();
            
            //Ajout de l'étudiant au module
            unModule.getEtudiants().add(unEtudiant);
            
            //Vérification que l'étudiant a été ajouté au module
            assertEquals(count + 1, unModule.getEtudiants().size());
            assertTrue(unModule.getEtudiants().contains(unEtudiant));
        }
	}

}
