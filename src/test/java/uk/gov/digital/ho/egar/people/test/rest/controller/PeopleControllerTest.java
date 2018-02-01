package uk.gov.digital.ho.egar.people.test.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import javax.ws.rs.core.MediaType;
import org.springframework.http.MediaType;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import uk.gov.digital.ho.egar.people.PeopleApiApplication;
import uk.gov.digital.ho.egar.people.api.PeopleRestService;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(properties = { "eureka.client.enabled=false", "spring.cloud.config.discovery.enabled=false",
		"spring.profiles.active=test" })
@AutoConfigureMockMvc
public class PeopleControllerTest {

	protected final Logger logger = LoggerFactory.getLogger(PeopleControllerTest.class);
	@Autowired
    private PeopleApiApplication app;
	
	@Autowired
	private MockMvc mockMvc;

	private static String PATHTOTEST = PeopleRestService.ROOT_PATH + PeopleRestService.PATH_PEOPLE;
	private static final String USER_HEADER_NAME = "x-auth-subject";
	String JWTToken = "3f3d62a5-72c4-4265-bf19-5a91a6caabb5";
	
	
	@Test
	public void shouldLoadContext() {
		logger.info("TEST: Checking if application loads");
		assertThat(app).isNotNull();
	}
	
	
	@Test
	public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception  {
		logger.info("TEST NOT AUTHENTICATED: " + PATHTOTEST);
		mockMvc.perform(MockMvcRequestBuilders.get(PATHTOTEST)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status()
				.is4xxClientError());
	}
	
	@Test
	public void shouldAllowAccessToAuthenticatedUsersToAddPerson() throws Exception {
		String addPerson = new JSONObject().put("given_name", "hello").toString();
		
		mockMvc.perform(post(PATHTOTEST)
				.header(USER_HEADER_NAME,  JWTToken )
				.contentType(MediaType.APPLICATION_JSON)
				.content(addPerson)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(303));
		
	}
	
	@Test
	public void shouldNotGetPersonWitoutUserID() throws Exception {
		shouldAllowAccessToAuthenticatedUsersToAddPerson();
		
		mockMvc.perform(get(PATHTOTEST + "39938464ted"))
				.andExpect(status().is(401));
		
	}
	
	@Test
	public void shouldNotAllowAnUpdateWithPut() throws Exception {
		String updatePersonWithPut = new JSONObject().put("type", "PILOT").toString();
		mockMvc.perform(put(PATHTOTEST)
				.header(USER_HEADER_NAME,  JWTToken )
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatePersonWithPut)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(405));
		
	}
		

}
