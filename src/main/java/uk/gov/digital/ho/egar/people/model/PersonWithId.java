package uk.gov.digital.ho.egar.people.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.digital.ho.egar.people.service.repository.model.PersonPersistentRecord;

import java.util.UUID;

@JsonDeserialize(as = PersonPersistentRecord.class)
public interface PersonWithId extends Person {

    @JsonProperty("person_uuid")
	public UUID getPersonUuid();

	@JsonIgnore
	public UUID getUserUuid();
}
