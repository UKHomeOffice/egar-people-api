package uk.gov.digital.ho.egar.people.service.repository.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.*;
import lombok.*;
import uk.gov.digital.ho.egar.people.model.Gender;
import uk.gov.digital.ho.egar.people.model.PersonWithId;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PERSON")
public class PersonPersistentRecord implements PersonWithId {

	@Id
    private UUID personUuid;

    @Column
    private String givenName;

    @Column
    private String familyName;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String address;

    @Column
    private LocalDate dob;

    @Column
    private String place;

    @Column
    private String nationality;

    @Column
    private String documentType;

    @Column
    private String documentNo;

    @Column
    private LocalDate documentExpiryDate;

    @Column
    private String documentIssuingCountry;

    @Column
    protected UUID userUuid;
    
}
