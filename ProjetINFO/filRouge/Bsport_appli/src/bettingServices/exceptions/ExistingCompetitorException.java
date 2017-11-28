package fr.uv1.bettingServices.exceptions;

public class ExistingCompetitorException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExistingCompetitorException(String s) {
		super(s);
	}
}