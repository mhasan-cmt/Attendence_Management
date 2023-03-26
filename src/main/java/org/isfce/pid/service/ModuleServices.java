package org.isfce.pid.service;

import java.util.Optional;
import java.util.List;

import org.isfce.pid.dao.IInscriptionJpaDao;
import org.isfce.pid.dao.IModuleJpaDao;
import org.isfce.pid.dao.IProfesseurJpaDao;
import org.isfce.pid.model.Etudiant;
import org.isfce.pid.model.Inscription;
import org.isfce.pid.model.Module;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.isfce.pid.controller.exceptions.CanNotDeleteException;

@Transactional
@Service
public class ModuleServices {
	
	private IModuleJpaDao moduleJpaDao;
	private IProfesseurJpaDao professeurJpaDao;
	private IInscriptionJpaDao inscriptionJpaDao;
	
	public ModuleServices(IProfesseurJpaDao iprofesseurJpaDao, IModuleJpaDao imoduleJpaDao, IInscriptionJpaDao inscriptionJpaDao)
	{
		this.moduleJpaDao = imoduleJpaDao;
		this.professeurJpaDao = iprofesseurJpaDao;
		this.inscriptionJpaDao = inscriptionJpaDao;
	}
	
	public String generateCodeModule(String c)
	{
		return moduleJpaDao.generateCodeModule(c);
	}

	@Transactional 
	public List<Module> findAll(){
		return this.moduleJpaDao.findAll();
	}
	
	@Transactional 
	public Optional<Module> findByCode(String code){
		return this.moduleJpaDao.findModuleCode(code);
	}
	
	@Transactional 
	public boolean exist(String code) {
		Optional<Module> module = moduleJpaDao.findModuleCode(code);
		/*List<Module> modules = this.moduleJpaDao.findAll();
		for (Module m: modules)
		{
			if (m.getCode().equals(code))
				return true;
		}
		return false;//*/
		return module.isPresent() && module.get().getCode().equals(code);
		// existsByCode(code);
	}
	
	@Transactional 
	public Optional<Module>  findOne(String code){
		/*List<Module> modules = this.moduleJpaDao.findAll();
		for (Module m: modules)
		{
			if (m.getCode().equals(code))
			{
				return Optional.ofNullable(m);
			}
		}
		return Optional.empty();//*/
		// findByCode(code);
		return moduleJpaDao.findModuleCode(code);
	}
	
	@Transactional 
	public void delete(String code) throws CanNotDeleteException {
		moduleJpaDao.deleteModule(code);
	}
	
	@Transactional 
	public void deleteModuleInscription(String code) throws CanNotDeleteException {
		inscriptionJpaDao.deleteInscriptionsByModuleCode(code);
		moduleJpaDao.deleteModule(code);
	}
	
	@Transactional 
	public List<Module>  getModulesProf(String username){
		return this.moduleJpaDao.getModuleProfesseur(username);
	}

	/**
	 * Ajout d'un nouveau cours
	 * 
	 * @param c1
	 * @return
	 */
	@Transactional 
	public Module insert(Module modules) {
		// TODO Auto-generated method stub
		return update(modules);
	}
	
	/***
	 * Fait une mise à  jour d'un cours existant le code du cours ne doit pas
	 * changer! Si le cours n'existe pas, il sera rajouté
	 * 
	 * @param c1
	 */
	@Transactional 
	public Module update(Module module) {
		assert module != null : "Le module doit exister";
		return moduleJpaDao.save(module);
	}
	
	/*public List<Inscription> findEtudiantsByModuleCode(String moduleCode)
	{
		return this.inscriptionJpaDao.findInscriptionByModuleCode(moduleCode);
	}*/
}
 