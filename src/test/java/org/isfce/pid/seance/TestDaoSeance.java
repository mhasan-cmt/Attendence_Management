package org.isfce.pid.seance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.isfce.pid.dao.IModuleJpaDao;
import org.isfce.pid.model.Module;
import org.isfce.pid.model.Seance;
import org.isfce.pid.dao.ISeanceJpaDao;
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
public class TestDaoSeance {
    @Autowired
    private ISeanceJpaDao seanceJpaDao;
    @Autowired
    private IModuleJpaDao moduleJpaDao;

    @Transactional
    @Test
    public void testFindSeanceByModuleCode() {
        List<Seance> seances = seanceJpaDao.findByModuleCode("IPID-1-A");

        assertThat(seances).hasSize(0);
    }

    @Transactional
    @Test
    public void testFindSeanceById() {
        Optional<Seance> foundSeance = seanceJpaDao.findSeanceById(1L);

        assertTrue(!foundSeance.isPresent());
    }
}
