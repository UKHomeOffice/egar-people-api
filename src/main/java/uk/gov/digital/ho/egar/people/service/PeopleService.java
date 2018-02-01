package uk.gov.digital.ho.egar.people.service;

import java.util.UUID;

import uk.gov.digital.ho.egar.people.api.exceptions.PersonNotFoundPersonApiException;
import uk.gov.digital.ho.egar.people.model.Person;
import uk.gov.digital.ho.egar.people.model.PersonWithId;


public interface PeopleService {
	
	public PersonWithId addPerson(final Person newPerson, final UUID uuid);
	public PersonWithId getPerson(final UUID uuid, final UUID userUuid) throws PersonNotFoundPersonApiException;
	public PersonWithId updatePerson(final UUID uuid, final Person updatePerson, final UUID userUuid) throws PersonNotFoundPersonApiException;
	public void deletePerson(final UUID personUuid, final UUID userUuid) throws PersonNotFoundPersonApiException;

}
