package uk.gov.digital.ho.egar.people;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.digital.ho.egar.people.test.utils.FileReaderUtils.readFileAsString;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import uk.gov.digital.ho.egar.people.api.PeopleRestService;
import uk.gov.digital.ho.egar.people.model.Gender;
import uk.gov.digital.ho.egar.people.service.repository.PeopleRepository;
import uk.gov.digital.ho.egar.people.service.repository.model.PersonPersistentRecord;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class EndpointTest {

	private static final String USERID_HEADER = "x-auth-subject";
	private static final String AUTH_HEADER = "Authorization";

	private static final UUID USER_UUID = UUID.randomUUID();
	private static final String AUTH = "TEST";

	private static final String PERSONS_ENDPOINT = "/api/v1/persons/";




	@Autowired
	private PeopleRepository repo;

	@Autowired
	private PeopleApiApplication app;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
		assertThat(app).isNotNull();
	}

	@Test
	public void jpaSchemaCanBeMappedFromClasses() {
		assertThat(this.repo).isNotNull();
	}
	//------------------------------------------------------------------------------------------
	/*
	 *  POST - CREATE
	 */
	//------------------------------------------------------------------------------------------
	@Test
	public void successfullyCreateAPerson() throws Exception {
		// WITH
		String jsonPersonData = readFileAsString("files/SuccessCreateAPersonRequest.json");
		// WHEN
		MvcResult result = this.mockMvc
				.perform(post(PERSONS_ENDPOINT)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header(AUTH_HEADER, AUTH)
						.header(USERID_HEADER, USER_UUID)
						.content(jsonPersonData))
				.andDo(print())
				.andExpect(status().isSeeOther())
				.andReturn();

		UUID personUuid = getPersonUUID(result);

		PersonPersistentRecord repoPerson = repo.findOne(personUuid);
		// THEN
		assertNotNull(repoPerson);
		assertEquals(repoPerson.getPersonUuid(), 				personUuid);
		assertEquals(repoPerson.getGivenName(), 				"hello");
		assertEquals(repoPerson.getFamilyName(), 				"world");
		assertTrue(repoPerson.getGender()					==	Gender.MALE);
		assertEquals(repoPerson.getAddress(), 					"124ABC");
		assertTrue(repoPerson.getDob() 					.equals(LocalDate.of(1995, 12, 16)));
		assertEquals(repoPerson.getPlace(), 					"Bath");
		assertEquals(repoPerson.getNationality(), 				"SCO");
		assertEquals(repoPerson.getDocumentType(), 				"ID");
		assertEquals(repoPerson.getDocumentNo(),				"1a2b3c4d");
		assertTrue(repoPerson.getDocumentExpiryDate()	.equals(LocalDate.of(2023, 12, 04)));
		assertEquals(repoPerson.getDocumentIssuingCountry(), 	"AUT");
	}

	@Ignore //TODO what will throw a 400??
	@Test
	public void badRequestCreatingNewPerson()  throws Exception{

	}

	@Test
	public void cannotCreateWithOutUserAuth() throws Exception {

		// WITH
		String jsonPersonData = readFileAsString("files/SuccessCreateAPersonRequest.json");
		// WHEN
		this.mockMvc
		.perform(post(PERSONS_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonPersonData))
		.andDo(print())
		// THEN
		.andExpect(status().isUnauthorized());         
	}

	@Test
	@Ignore
	public void unSuccessfullyCreateAPerson() throws Exception {
		// WITH
		String jsonPersonData = readFileAsString("files/FailCreateAPersonRequest.json");
		// WHEN
		this.mockMvc
		.perform(post(PERSONS_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(AUTH_HEADER, AUTH)
				.header(USERID_HEADER, USER_UUID)
				.content(jsonPersonData))
		.andDo(print())
		.andExpect(status().isBadRequest());
	}

	//------------------------------------------------------------------------------------------
	/*
	 *  POST - UPDATE
	 */
	//------------------------------------------------------------------------------------------
	@Test
	public void successfullyUpdateAPerson() throws Exception {
		// WITH
		PersonPersistentRecord person = PersonPersistentRecord.builder()
				.userUuid(USER_UUID)
				.personUuid(UUID.randomUUID())
				.givenName("Jenna")
				.familyName("Smith")
				.gender(Gender.FEMALE)
				.address("123 ABC")
				.dob(LocalDate.of(1995, 12, 12))
				.place("London")
				.nationality("GDB")
				.documentType("PASSPORT")
				.documentNo("1234aBCD")
				.documentExpiryDate(LocalDate.of(2020, 12, 12))
				.documentIssuingCountry("UK")
				.build();

		repo.saveAndFlush(person);

		String jsonPersonData = readFileAsString("files/SuccessCreateAPersonRequest.json");
		String url = PERSONS_ENDPOINT + person.getPersonUuid() + "/";
		// WHEN
		this.mockMvc
		.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(AUTH_HEADER, AUTH)
				.header(USERID_HEADER, USER_UUID)
				.content(jsonPersonData))
		.andDo(print())
		.andExpect(status().isSeeOther());

		PersonPersistentRecord repoUpdatedPerson = repo.findOne(person.getPersonUuid());
		// THEN
		assertNotNull(repoUpdatedPerson);
		assertEquals(repoUpdatedPerson.getPersonUuid(), 				person.getPersonUuid());
		assertEquals(repoUpdatedPerson.getGivenName(), 					"hello");
		assertEquals(repoUpdatedPerson.getFamilyName(), 				"world");
		assertTrue(repoUpdatedPerson.getGender()					==	Gender.MALE);
		assertEquals(repoUpdatedPerson.getAddress(), 					"124ABC");
		assertTrue(repoUpdatedPerson.getDob() 					 .equals(LocalDate.of(1995, 12, 16)));
		assertEquals(repoUpdatedPerson.getPlace(), 						"Bath");
		assertEquals(repoUpdatedPerson.getNationality(), 				"SCO");
		assertEquals(repoUpdatedPerson.getDocumentType(), 				"ID");
		assertEquals(repoUpdatedPerson.getDocumentNo(),					"1a2b3c4d");
		assertTrue(repoUpdatedPerson.getDocumentExpiryDate()	.equals(LocalDate.of(2023, 12, 04)));
		assertEquals(repoUpdatedPerson.getDocumentIssuingCountry(), 	"AUT");
	}

	@Test
	public void cannotUpdateWithOutUserAuth() throws Exception {

		// WITH
		String jsonPersonData = readFileAsString("files/SuccessCreateAPersonRequest.json");
		String jsonUpdatePersonData = readFileAsString("files/SuccessUpdateAPersonRequest.json");

		// WHEN
		MvcResult result = this.mockMvc
				.perform(post(PERSONS_ENDPOINT)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header(AUTH_HEADER, AUTH)
						.header(USERID_HEADER, USER_UUID)
						.content(jsonPersonData))
				.andDo(print())
				.andExpect(status().isSeeOther())
				.andReturn();

		UUID personUuid = getPersonUUID(result);

		PersonPersistentRecord repoPerson = repo.findOne(personUuid);
		// THEN
		assertNotNull(repoPerson);
		assertEquals(repoPerson.getPersonUuid(), personUuid);
		assertEquals(repoPerson.getGivenName(), "hello");
		assertEquals(repoPerson.getFamilyName(), "world");
		assertTrue(repoPerson.getGender()==Gender.MALE);

		// WHEN
		this.mockMvc
		.perform(post(PERSONS_ENDPOINT + personUuid + "/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonUpdatePersonData))
		.andDo(print())
		// THEN
		.andExpect(status().isUnauthorized());         
	}

	@Ignore //TODO what will throw a 400??
	@Test
	public void badRequestUpdatingNewPerson()  throws Exception{

	}
	//------------------------------------------------------------------------------------------
	/*
	 *  GET
	 */
	//------------------------------------------------------------------------------------------
	@Test
	public void successfullyRetrieveAPerson() throws Exception {
		// WITH
		PersonPersistentRecord person = PersonPersistentRecord.builder()
				.userUuid(USER_UUID)
				.personUuid(UUID.randomUUID())
				.givenName("Jenna")
				.familyName("Smith")
				.gender(Gender.FEMALE)
				.address("123 ABC")
				.dob(LocalDate.of(1995, 12, 12))
				.place("London")
				.nationality("GDB")
				.documentType("PASSPORT")
				.documentNo("1234aBCD")
				.documentExpiryDate(LocalDate.of(2020, 12, 12))
				.documentIssuingCountry("UKA")
				.build();

		repo.saveAndFlush(person);

		String url = PERSONS_ENDPOINT + person.getPersonUuid() + "/";
		// WHEN
		this.mockMvc
		.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(AUTH_HEADER, AUTH)
				.header(USERID_HEADER, USER_UUID))
		.andDo(print())
		// THEN
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.given_name",				is("Jenna")))
		.andExpect(jsonPath("$.family_name",			is("Smith")))
		.andExpect(jsonPath("$.gender", 				is("FEMALE")))
		.andExpect(jsonPath("$.address", 				is("123 ABC")))
		.andExpect(jsonPath("$.dob", 					is("1995-12-12")))
		.andExpect(jsonPath("$.place", 					is("London")))
		.andExpect(jsonPath("$.nationality", 			is("GDB")))
		.andExpect(jsonPath("$.document_type",			is("PASSPORT")))
		.andExpect(jsonPath("$.document_no", 			is("1234aBCD")))
		.andExpect(jsonPath("$.document_expiryDate", 	is("2020-12-12")))
		.andExpect(jsonPath("$.document_issuingCountry",is("UKA")));

	}

	@Test
	public void cannotRetrieveWithOutUserAuth() throws Exception {
		// WITH
		String jsonPersonData = readFileAsString("files/SuccessCreateAPersonRequest.json");
		MvcResult result = this.mockMvc
				.perform(post(PERSONS_ENDPOINT)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header(AUTH_HEADER, AUTH)
						.header(USERID_HEADER, USER_UUID)
						.content(jsonPersonData))
				.andDo(print())
				.andExpect(status().isSeeOther())
				.andReturn();

		UUID personUuid = getPersonUUID(result);
		// WHEN
		this.mockMvc
		.perform(get(PERSONS_ENDPOINT + personUuid + "/")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andDo(print())
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void cannotRetrieveImaginaryPerson() throws Exception {
		// WITH
		UUID personUuid = UUID.randomUUID();
		// WHEN
		this.mockMvc
		.perform(get(PERSONS_ENDPOINT + personUuid + "/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(AUTH_HEADER, AUTH)
				.header(USERID_HEADER, USER_UUID))
		.andDo(print())
		.andExpect(status().isBadRequest());
	}

	//------------------------------------------------------------------------------------------
	/*
	 *  DELETE
	 */
	//------------------------------------------------------------------------------------------

	@Test
	public void succesfullyDeleteAPerson() throws Exception{
		repo.deleteAll();
		// WITH
		PersonPersistentRecord person = PersonPersistentRecord.builder()
				.userUuid(USER_UUID)
				.personUuid(UUID.randomUUID())
				.givenName("Jenna")
				.familyName("Smith")
				.gender(Gender.FEMALE)
				.address("123 ABC")
				.dob(LocalDate.of(1995, 12, 12))
				.place("London")
				.nationality("UK")
				.documentType("PASSPORT")
				.documentNo("1234aBCD")
				.documentExpiryDate(LocalDate.of(2020, 12, 12))
				.documentIssuingCountry("UK")
				.build();

		repo.saveAndFlush(person);

		String url = PERSONS_ENDPOINT + person.getPersonUuid() + "/";
		// WHEN
		this.mockMvc
		.perform(delete(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(AUTH_HEADER, AUTH)
				.header(USERID_HEADER, USER_UUID))
		// THEN
		.andExpect(status().isAccepted());

		assertTrue(repo.findAll().size() == 0);
	}

	@Test
	public void noMatchDeletingImaginaryGar() throws Exception {
		// WITH
		repo.deleteAll();
		String url = PERSONS_ENDPOINT + UUID.randomUUID().toString() + "/";
		// WHEN
		this.mockMvc
		.perform(delete(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(AUTH_HEADER, AUTH)
				.header(USERID_HEADER, USER_UUID))
		// THEN
		.andExpect(status().isBadRequest());
	}
	@Ignore //TODO whenAuthentication is implemented
	@Test
	public void unauthorizedDeletingImaginaryGar() throws Exception {
		// WITH
		String url = PERSONS_ENDPOINT + UUID.randomUUID().toString() + "/";
		// WHEN
		this.mockMvc
		.perform(delete(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(AUTH_HEADER, AUTH)
				.header(USERID_HEADER, USER_UUID))
		// THEN
		.andExpect(status().isUnauthorized());
	}

	//------------------------------------------------------------------------------------------
	/*
	 *  VALIDATION TESTS
	 */
	//------------------------------------------------------------------------------------------
	@Test
	public void peopleValidationTest() throws Exception {
		String request = readFileAsString("files/FailCreateAPersonRequest.json");
		String resp = badRequestResponseContent(request);
		with(resp).assertThat("$.message[*]", hasItems(
				"givenName: size must be between 0 and 35",
				"documentIssuingCountry: size must be between 0 and 3",
				"nationality: size must be between 0 and 3",
				"familyName: size must be between 0 and 35"));
	}

	private UUID getPersonUUID(MvcResult result){
		String location = result.getResponse().getHeader("location");
		String[] parts = location.split("/");
		return UUID.fromString(parts[parts.length-1]);
	}

	private String badRequestResponseContent(String json) throws Exception {
		MvcResult result = this.mockMvc
				.perform(post(PeopleRestService.ROOT_PATH + PeopleRestService.PATH_PEOPLE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.header(AUTH_HEADER, AUTH)
						.header(USERID_HEADER, USER_UUID)
						.content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();

		return result.getResponse().getContentAsString();
	}


}