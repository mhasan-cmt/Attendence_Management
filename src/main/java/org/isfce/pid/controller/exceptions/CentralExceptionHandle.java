package org.isfce.pid.controller.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.isfce.pid.controller.ModuleController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Didier 
 * Une classe annotée par @ControllerAdvice permet de fournir pour tous les controleurs, 
 *         -des gestions (déviations) d'exceptions
 *         centalisées 
 *         -s'assurer que certaines clés soient présentes dans
 *         "model" avec @ModelAttribute 
 *         -créer des binders pour permettre une
 *         conversion de String vers une classe (ex: reconstruire une date à
 *         partir d'un texte)
 */
@ControllerAdvice
@Slf4j
public class CentralExceptionHandle {
	/**
	 * Déviation des exceptions citées pour ouvrir une page personnalisée de type
	 * erreur Comme il n'est pas possible de fournir une Map à cette méthode, on
	 * doit créer un objet "ModelAndView"
	 * 
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler({NoSuchElementException.class, NotExistException.class,DuplicateException.class})
	private ModelAndView generalHandler(HttpServletRequest req, Exception e) {
		ModelAndView m = new ModelAndView();
		
		/*log.debug("Debug information", e);
		
		Map<String, Object> informations = new HashMap<>();
	    informations.put("message", e.getMessage());
	    informations.put("code", e instanceof NotExistException ? ((NotExistException) e).getCode() : null);
	    informations.put("path", req.getRequestURL());
	    
	    log.debug("Debug information", informations);
	    
	    m.addObject("informations", informations);*/
	    
	    m.addObject("message", e.getMessage());
	    m.addObject("code", e instanceof NotExistException ? ((NotExistException) e).getCode() : null);
	    m.addObject("path", req.getRequestURL());
		
		m.setViewName("error");//nom logique de la page d'erreur
		return m;
	}	
}
