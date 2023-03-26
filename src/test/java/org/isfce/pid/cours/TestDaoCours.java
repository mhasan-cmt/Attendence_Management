package org.isfce.pid.cours;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import javax.transaction.Transactional;

import org.isfce.pid.dao.ICoursJpaDao;
import org.isfce.pid.model.Cours;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles(value = "testU")
@Sql({ "/dataTestU.sql" })
@SpringBootTest
class TestDaoCours {
	@Autowired
	ICoursJpaDao daoCours;

	@Test
	@Transactional
	void countCours() {
		assertEquals(7, daoCours.count());
	}

	@Test
	@Transactional
	void getCours() {
		Optional<Cours> oipid = daoCours.findById("IPID");
		assertTrue(oipid.isPresent());
		assertTrue(oipid.get().getSections().contains("Informatique"));
	}
}
