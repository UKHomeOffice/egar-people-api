package uk.gov.digital.ho.egar.people.api;
public interface PeopleApiResponse {
	
	public static final String SWAGGER_MESSAGE_SUCCESSFUL_RETRIEVED_KEY 		= "Successfully retrieved."; //200
	public static final String SWAGGER_MESSAGE_SUCCESSFUL_DELETED_KEY 			= "Successfully DELETED."; //202
	public static final String SWAGGER_MESSAGE_SUCCESSFUL_UPDATE_REDIRECT_KEY 	= "Successfully updated, please follow redirect."; //303
	public static final String SWAGGER_MESSAGE_SUCCESSFUL_ADDED_REDIRECT_KEY 	= "Successfully added, please follow redirect."; //303
	public static final String SWAGGER_MESSAGE_NOT_FOUND 						= "Person not Found"; //400
    public static final String SWAGGER_MESSAGE_UNAUTHORISED 					= "Unauthorised"; //401
    public static final String SWAGGER_MESSAGE_FORBIDDEN 						= "Forbidden"; //403

}