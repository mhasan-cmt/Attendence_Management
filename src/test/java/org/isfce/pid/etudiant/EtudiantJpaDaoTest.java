package org.isfce.pid.etudiant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.isfce.pid.dao.IEtudiantJpaDao;
import org.isfce.pid.model.Etudiant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles(value = "testU")
@SpringBootTest
@Sql({"/dataTestU.sql"})
public class EtudiantJpaDaoTest {

    @Autowired
    private IEtudiantJpaDao etudiantDao;

    @Test
    @Transactional
    public void testFindByUsername() {
        Optional<Etudiant> optionalEtudiant = etudiantDao.findByUsername("et1");
        assertTrue(optionalEtudiant.isPresent());
    }
    
    @Test
    @Transactional
    public void testFindById() {
        Optional<Etudiant> optionalEtudiant = etudiantDao.findById(1);
        assertTrue(optionalEtudiant.isPresent());
    }

    @Test
    @Transactional
    public void testGetListEtudiantUsername() {
        List<String> list = etudiantDao.getListEtudiantUsername();
        assertFalse(list.isEmpty());
    }

    @Test
    @Transactional
    public void testExistByUserName() {
        boolean exist = etudiantDao.existByUserName("et1");
        assertTrue(exist);
    }

    @Test
    @Transactional
    public void testCountByNomAndPrenom() {
        int count = etudiantDao.countByNomAndPrenom("Nom_ET1", "Prenom_ET1");
        assertEquals(1, count);
    }

}
