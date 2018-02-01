package uk.gov.digital.ho.egar.people.service.repository.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.digital.ho.egar.people.model.Gender;


public class PersonPersistentRecordTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void initMapper() {
        mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
        mapper.findAndRegisterModules();
    }

    @Test
    public void canCreateJsonAndReRead() throws Exception {


        String jsonPerson = "{\n" +
                "  \"given_name\" : \"hello\",\n" +
                "  \"family_name\" : \"world\",\n" +
                "  \"gender\": \"MALE\",\n" +
                "  \"address\": \"124ABC\",\n" +
                "  \"dob\": \"1995-12-16\",\n" +
                "  \"place\": \"Bath\",\n" +
                "  \"nationality\": \"Scottish\",\n" +
                "  \"document_type\": \"ID\",\n" +
                "  \"document_no\": \"1a2b3c4d\",\n" +
                "  \"document_expiryDate\": \"2023-12-04\",\n" +
                "  \"document_issuingCountry\": \"Austria\"\n" +
                "}";

        PersonPersistentRecord record = mapper.readValue(jsonPerson, PersonPersistentRecord.class);
        assertEquals(record.getGivenName(), "hello");
        assertEquals(record.getFamilyName(), "world");
        assertEquals(record.getGender(), Gender.MALE);
        assertEquals(record.getAddress(), "124ABC");
        assertEquals(record.getDob(), LocalDate.of(1995,12, 16));
        assertEquals(record.getPlace(), "Bath");
        assertEquals(record.getNationality(), "Scottish");
        assertEquals(record.getDocumentType(), "ID");
        assertEquals(record.getDocumentNo(), "1a2b3c4d");
        assertEquals(record.getDocumentExpiryDate(), LocalDate.of(2023, 12, 04));
        assertEquals(record.getDocumentIssuingCountry(), "Austria");
    }
}