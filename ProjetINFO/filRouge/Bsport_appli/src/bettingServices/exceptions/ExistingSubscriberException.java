package fr.uv1.bettingServices.exceptions;

public class ExistingSubscriberException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExistingSubscriberException(String s) {
		super(s);
	}
}