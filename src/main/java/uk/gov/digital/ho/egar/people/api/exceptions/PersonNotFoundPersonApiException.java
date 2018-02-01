package uk.gov.digital.ho.egar.people.api.exceptions;

import java.util.UUID;

public class PersonNotFoundPersonApiException extends DataNotFoundPersonApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonNotFoundPersonApiException (final UUID personUuid, final UUID userUuid) {
		super(String.format("Can not find person '%s' for user '%s'.", personUuid, userUuid));
	}
	
}
