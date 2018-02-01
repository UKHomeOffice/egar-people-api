package uk.gov.digital.ho.egar.people.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.digital.ho.egar.people.api.exceptions.PersonNotFoundPersonApiException;
import uk.gov.digital.ho.egar.people.model.Person;
import uk.gov.digital.ho.egar.people.model.PersonWithId;
import uk.gov.digital.ho.egar.people.service.repository.model.PersonPersistentRecord;
import uk.gov.digital.ho.egar.people.service.PeopleService;
import uk.gov.digital.ho.egar.people.service.repository.PeopleRepository;


@Service
public class PeopleServiceDatabaseImpl implements PeopleService {
	private static final Logger logger = LoggerFactory.getLogger(PeopleServiceDatabaseImpl.class);
	
	@Autowired
	private PeopleRepository peopleRepository;
	

	@Override
	public PersonWithId addPerson(Person newPerson, UUID userUuid) {
		PersonPersistentRecord peopleServicePojo = createPerson(UUID.randomUUID(), userUuid, newPerson);
		logger.debug("Person added with ID " + peopleServicePojo.getPersonUuid());

		return peopleRepository.save(peopleServicePojo);
	}

	@Override
	public PersonWithId getPerson(UUID personUuid, UUID userUuid) throws PersonNotFoundPersonApiException {
		logger.debug("Getting person with ID " + userUuid.toString());
		PersonPersistentRecord person = peopleRepository.findOneByPersonUuidAndUserUuid(personUuid, userUuid);
		if (person==null){
			throw new PersonNotFoundPersonApiException(personUuid, userUuid);
		}
        return person;
	}

	@Override
	public PersonWithId updatePerson(UUID personUuid, Person personDetails, UUID userUuid) throws PersonNotFoundPersonApiException {
		logger.debug("Updating person with ID " + personUuid.toString());

		PersonWithId existingPerson = getPerson(personUuid, userUuid);

        PersonPersistentRecord updatedPerson = createPerson(existingPerson.getPersonUuid(), userUuid, personDetails);

        return peopleRepository.save(updatedPerson);
	}

	@Override
	public void deletePerson(UUID personUuid, UUID userUuid) throws PersonNotFoundPersonApiException  {
		
		logger.debug("Deleting person with ID " + personUuid.toString());
		
		PersonPersistentRecord person = peopleRepository.findOneByPersonUuidAndUserUuid(personUuid, userUuid);
		if (person==null){
			throw new PersonNotFoundPersonApiException(personUuid, userUuid);
		}
		peopleRepository.delete(personUuid);
		
	}

	private PersonPersistentRecord createPerson(UUID personUuid, UUID userUuid, Person details){
        return PersonPersistentRecord.builder()
                .address(details.getAddress())
                .dob(details.getDob())
                .documentExpiryDate(details.getDocumentExpiryDate())
                .documentIssuingCountry(details.getDocumentIssuingCountry())
                .documentNo(details.getDocumentNo())
                .documentType(details.getDocumentType())
                .familyName(details.getFamilyName())
                .givenName(details.getGivenName())
                .gender(details.getGender())
                .nationality(details.getNationality())
                .place(details.getPlace())
                .userUuid(userUuid)
                .personUuid(personUuid)
                .build();
    }

}
