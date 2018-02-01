package uk.gov.digital.ho.egar.people.service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.egar.people.service.repository.model.PersonPersistentRecord;

@Transactional
public interface PeopleRepository extends JpaRepository<PersonPersistentRecord, UUID>{
	PersonPersistentRecord findOneByPersonUuidAndUserUuid(UUID personUuid, UUID userUuid);
}
