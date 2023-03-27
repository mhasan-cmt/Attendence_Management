package org.isfce.pid.dao;

import org.isfce.pid.model.Certificat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICertificatJpaDao extends JpaRepository<Certificat, Long>  {
    Optional<Certificat> findByEtudiantId(Integer id);
}
