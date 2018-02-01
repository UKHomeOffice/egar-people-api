package uk.gov.digital.ho.egar.people.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST) 
public abstract class DataNotFoundPersonApiException extends PersonApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DataNotFoundPersonApiException() {
	}

	/**
	 * @param message
	 */
	public DataNotFoundPersonApiException(String message) {
		super(message);
	}

}
