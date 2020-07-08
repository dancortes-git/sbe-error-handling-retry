package br.com.dcc.springbatchexamples.exception;

public class ErrorHandlingException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ErrorHandlingException(String message) {
		super(message);
	}
}
