package org.isfce.pid.dao;

import org.isfce.pid.model.Certificat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICertificatJpaDao extends JpaRepository<Certificat, Long>  {
}
