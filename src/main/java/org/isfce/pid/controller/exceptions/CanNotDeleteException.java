package org.isfce.pid.controller.exceptions;

public class CanNotDeleteException extends Exception{
	private static final long serialVersionUID = 1L;
	// identifiant de l'objet recherche
	private String code;
	
	public CanNotDeleteException(String code) {
		super();
		this.code = code;
	}
	
}
