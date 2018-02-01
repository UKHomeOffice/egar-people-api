package uk.gov.digital.ho.egar.people.model;

import java.time.LocalDate;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.digital.ho.egar.people.service.repository.model.PersonPersistentRecord;

@JsonDeserialize(as = PersonPersistentRecord.class)
public interface Person {
	// EGAR 1388
    @Size(max = 35)
	@JsonProperty("given_name")
	public String getGivenName();
	@Size(max = 35)
	@JsonProperty("family_name")
	public String getFamilyName();
	public Gender getGender();
	@Size(max = 35)
	public String getAddress();
	public LocalDate getDob();
	@Size(max = 35)
	public String getPlace();
	@Size(max = 3)
	public String getNationality();
	@JsonProperty("document_type")
	public String getDocumentType();
	@Size(max = 44)
	@JsonProperty("document_no")
	public String getDocumentNo();
	@JsonProperty("document_expiryDate")
	public LocalDate getDocumentExpiryDate();
	@Size(max = 3)
	@JsonProperty("document_issuingCountry")
	public String getDocumentIssuingCountry();
}
