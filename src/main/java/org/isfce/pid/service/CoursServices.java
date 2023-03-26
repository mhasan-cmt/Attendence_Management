package org.isfce.pid.service;

import java.util.List;
import java.util.Optional;

//import javax.transaction.Transactional;

import org.isfce.pid.dao.ICoursJpaDao;
import org.isfce.pid.model.Cours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CoursServices {

	private ICoursJpaDao coursdao;

	public CoursServices(ICoursJpaDao coursdao) {
		this.coursdao = coursdao;
	}

	/**
	 * retourne la liste de tous les cours
	 * 
	 * @return collection de cours
	 */
	public List<Cours> findAll() {
		return coursdao.findAll();
	}

	/**
	 * Vérifie si un cours existe
	 * 
	 * @param code
	 * @return
	 */
	public boolean exists(String code) {

		return coursdao.existsById(code);

	}

	/**
	 * retourne un cours à partir de son code
	 * 
	 * @param code
	 * @return un Optional de cours
	 */
	public Optional<Cours> findOne(String code) {
		return coursdao.findById(code);
	}

	/**
	 * Suppression du cours s'il existe Il ne faut pas qu'il possède des liens vers
	 * des modules
	 * 
	 * @param code
	 */
	public boolean delete(String code) {
		if (code == null)
			return false;
		coursdao.deleteById(code);
		return true;
	}

	/***
	 * Fait une mise à  jour d'un cours existant le code du cours ne doit pas
	 * changer! Si le cours n'existe pas, il sera rajouté
	 * 
	 * @param c1
	 */
	public Cours update(Cours c1) {
		assert c1 != null : "Le cours doit exister";
		return coursdao.save(c1);
	}

	/**
	 * Ajout d'un nouveau cours
	 * 
	 * @param c1
	 * @return
	 */
	public Cours insert(Cours c1) {
		return update(c1);
	}

	/**
	 * Test s'il n'existe pas un autre cours avec le même nom
	 * 
	 * @param c1
	 * @return true s'il existe un autre cours avec le même nom que c1.getNom()
	 */
	public boolean testNomDoublon(Cours c1) {
		assert c1 != null : "Le cours doit exister";
		return coursdao.countByNom(c1.getNom()) > 0;
	}

}
