package uk.gov.digital.ho.egar.people.api.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import uk.gov.digital.ho.egar.shared.util.exceptions.NoCallStackException;

/**
 * A base exception type that does not pick uo the stack trace.
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Suppress empty arrays & nulls
public class PersonApiException extends NoCallStackException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonApiException() {
		this(null,null);		
	}

	public PersonApiException(String message) {
		this(message,null);
	}

	public PersonApiException(Throwable cause) {
		this(null,cause);
	}

	public PersonApiException(String message, Throwable cause) {
		super(message, cause);
        }

}

