package uk.gov.digital.ho.egar.people.api;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import uk.gov.digital.ho.egar.constants.ServicePathConstants;
import uk.gov.digital.ho.egar.people.api.exceptions.PersonNotFoundPersonApiException;
import uk.gov.digital.ho.egar.people.model.Person;
import uk.gov.digital.ho.egar.people.model.PersonWithId;

public interface PeopleRestService extends ServicePathConstants {
	
	
	public static final String ROOT_SERVICE_NAME = "persons";
	
	public static final String ROOT_PATH = 
			ServicePathConstants.ROOT_PATH_SEPERATOR + 
			ServicePathConstants.ROOT_SERVICE_API + 
			ServicePathConstants.ROOT_PATH_SEPERATOR +
			ServicePathConstants.SERVICE_VERSION_ONE +
			ServicePathConstants.ROOT_PATH_SEPERATOR + 
			ROOT_SERVICE_NAME;
	
	public static final String PATH_PEOPLE = "/";
	public static final String PATH_PERSON = "/{person_uuid}";
	public static final String PATH_BULK = "/summaries";
	
	public ResponseEntity<Void> addPerson( UUID uuidOfUser, Person newPerson ) throws URISyntaxException;
	
	public PersonWithId getPerson(UUID uuidOfUser, UUID uuid) throws PersonNotFoundPersonApiException;
	
	public ResponseEntity<Void> deletePerson(UUID uuidOfUser, UUID uuid) throws PersonNotFoundPersonApiException;
	public ResponseEntity<Void> updatePerson(UUID uuidOfUser, UUID uuid, Person updateThis) throws URISyntaxException, PersonNotFoundPersonApiException;

	public PersonWithId[] bulkRetrievePeople(final UUID uuidOfUser, final List<UUID> peopleUuids);
}
