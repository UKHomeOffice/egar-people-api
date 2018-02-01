package uk.gov.digital.ho.egar.people.api.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.gov.digital.ho.egar.people.api.PeopleApiResponse;
import uk.gov.digital.ho.egar.people.api.PeopleRestService;
import uk.gov.digital.ho.egar.people.api.exceptions.PersonApiException;
import uk.gov.digital.ho.egar.people.api.exceptions.PersonNotFoundPersonApiException;
import uk.gov.digital.ho.egar.people.model.Person;
import uk.gov.digital.ho.egar.people.model.PersonWithId;
import uk.gov.digital.ho.egar.people.service.PeopleService;

import javax.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestController
@RequestMapping(PeopleRestService.ROOT_PATH)
@Api(value = PeopleRestService.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PeopleRestController implements PeopleRestService  {
	 
	private static final Logger logger = LoggerFactory.getLogger(PeopleRestController.class);

	private static final String USER_HEADER_NAME = "x-auth-subject";

	@Autowired
	private PeopleService peopleService ;

	/**
	 * A quick & dirty fix to change 400 due to a ServletRequestBindingException to a 401.
	 * 99% of expected ServletRequestBindingException will be due to a missing header.
	 */
	@ExceptionHandler(ServletRequestBindingException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody
	PersonApiException handleException(ServletRequestBindingException ex) {
		return new PersonApiException(ex.getMessage()) ;
	}

	
	/**
	 * Add Person (record) - exceptions needed for 
	 * @throws URISyntaxException 
	 */
	@ApiOperation(value = "Add A New Person.", notes = "Add a new person record")
	@ApiResponses(value = {
			@ApiResponse(code = 303, message = PeopleApiResponse.SWAGGER_MESSAGE_SUCCESSFUL_ADDED_REDIRECT_KEY),
			@ApiResponse(code = 400, message = PeopleApiResponse.SWAGGER_MESSAGE_NOT_FOUND),
			@ApiResponse(code = 401, message = PeopleApiResponse.SWAGGER_MESSAGE_UNAUTHORISED),
			@ApiResponse(code = 403, message = PeopleApiResponse.SWAGGER_MESSAGE_FORBIDDEN) })
	@ResponseStatus(HttpStatus.SEE_OTHER)
	@Override
	@PostMapping(PeopleRestService.PATH_PEOPLE)
	public ResponseEntity<Void> addPerson(@RequestHeader(USER_HEADER_NAME) UUID uuidOfUser, @Valid @RequestBody Person newPerson) throws URISyntaxException {
	 	
		logger.debug("EGAR: Adding person");
		PersonWithId persistedPerson = peopleService.addPerson(newPerson, uuidOfUser);
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(getRedirectURIForPerson(persistedPerson.getPersonUuid()));
        return new ResponseEntity<Void>(responseHeaders, HttpStatus.SEE_OTHER);
	}
	

	
	/**
	 * Get Person based on UUID.
	 */

	@ApiOperation(value = "Get A Exisitng Person.", notes = "Get an exisiting person")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = PeopleApiResponse.SWAGGER_MESSAGE_SUCCESSFUL_RETRIEVED_KEY),
			@ApiResponse(code = 400, message = PeopleApiResponse.SWAGGER_MESSAGE_NOT_FOUND, response = PersonNotFoundPersonApiException.class),
			@ApiResponse(code = 401, message = PeopleApiResponse.SWAGGER_MESSAGE_UNAUTHORISED),
			@ApiResponse(code = 403, message = PeopleApiResponse.SWAGGER_MESSAGE_FORBIDDEN) })
	@ResponseStatus(HttpStatus.OK)
	@Override
	@GetMapping(PeopleRestService.PATH_PERSON)
	public PersonWithId getPerson(@RequestHeader(USER_HEADER_NAME) UUID uuidOfUser, @PathVariable("person_uuid") UUID id) throws PersonNotFoundPersonApiException {
		logger.debug("EGAR: Getting person");
		PersonWithId person = peopleService.getPerson(id, uuidOfUser);
		return person;
		
		
	}

	/**
	 * Update Person Record.
	 * @param
	 * @throws URISyntaxException 
	 */
	@ApiOperation(value = "Update A Person.", notes = "Update a person record")
	@ApiResponses(value = {
			@ApiResponse(code = 303, message = PeopleApiResponse.SWAGGER_MESSAGE_SUCCESSFUL_UPDATE_REDIRECT_KEY),
			@ApiResponse(code = 400, message = PeopleApiResponse.SWAGGER_MESSAGE_NOT_FOUND),
			@ApiResponse(code = 401, message = PeopleApiResponse.SWAGGER_MESSAGE_UNAUTHORISED),
			@ApiResponse(code = 403, message = PeopleApiResponse.SWAGGER_MESSAGE_FORBIDDEN) })
	@ResponseStatus(HttpStatus.SEE_OTHER)
	@Override
	@PostMapping(value = PeopleRestService.PATH_PERSON)
	public ResponseEntity<Void> updatePerson(
			@RequestHeader(USER_HEADER_NAME) UUID uuidOfUser,
			@PathVariable("person_uuid")  UUID id,
			@Valid @RequestBody Person updatePerson) throws URISyntaxException, PersonNotFoundPersonApiException {

		logger.debug("EGAR: Updating person");
		PersonWithId persistedPerson = peopleService.updatePerson(id, updatePerson, uuidOfUser);
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(getRedirectURIForPerson(persistedPerson.getPersonUuid()));
        return new ResponseEntity<Void>(responseHeaders, HttpStatus.SEE_OTHER);

	}
	/**
	 * Delete Person Record. To be implemented later
	 * @throws PersonNotFoundPersonApiException 
	 */

	@ApiOperation(value = "Delete a person.", notes = "Delete a person record")
	@ApiResponses(value = {
			@ApiResponse(code = 202, message = PeopleApiResponse.SWAGGER_MESSAGE_SUCCESSFUL_DELETED_KEY),
			@ApiResponse(code = 400, message = PeopleApiResponse.SWAGGER_MESSAGE_NOT_FOUND),
			@ApiResponse(code = 401, message = PeopleApiResponse.SWAGGER_MESSAGE_UNAUTHORISED),
			@ApiResponse(code = 403, message = PeopleApiResponse.SWAGGER_MESSAGE_FORBIDDEN) })
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Override
	@DeleteMapping(path = PeopleRestService.PATH_PERSON)
	public ResponseEntity<Void> deletePerson(@RequestHeader(USER_HEADER_NAME) UUID uuidOfUser,
											 @PathVariable("person_uuid")  UUID personUuid) throws PersonNotFoundPersonApiException {
		logger.debug("EGAR: Deleting person");
		peopleService.deletePerson(personUuid, uuidOfUser);
		
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
	
    
    /**
     * Get redirection URI from path and person uuid
     * @throws URISyntaxException 
     * 
     */
    
    private  URI getRedirectURIForPerson(final UUID personUuid) throws URISyntaxException {

		return new URI(PeopleRestService.ROOT_PATH + "/" + personUuid);
    	
    }
    
    public static class ApiErrors {

        @JsonProperty("message")
        private final List<String> errorMessages = new ArrayList<>();

        public ApiErrors()
        {        	
        }
        
        public ApiErrors(Errors errors) {
        	
        	addFieldErrors(errors.getFieldErrors());
            addObjectErrors(errors.getGlobalErrors());
        }

		protected ApiErrors addObjectErrors(List<ObjectError> globalErrors) {
            for(final ObjectError error : globalErrors ){
                errorMessages.add(error.getObjectName() + ": " + error.getDefaultMessage());
            }
            return this ;
		}

		protected ApiErrors addFieldErrors(List<FieldError> fieldErrors) {
			
            for(final FieldError error : fieldErrors ){
                errorMessages.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return this ;
			
		}
    }
    
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrors processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
 
        return new ApiErrors().addFieldErrors(fieldErrors) ;
    }
}
