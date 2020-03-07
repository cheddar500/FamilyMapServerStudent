package fms.Responses;

import fms.JsonSerializer;

/**
 * Class with info about the Response of a RegisterRequest
 */
public class RegisterResponse implements IResponse{

    /**
     * AuthToken for the user
     */
    private String authToken;
    /**
     * Tells success/error messages
     */
    private String message;
    /**
     * identifier for the user
     */
    private String personID;
    /**
     * Tells if completed successfully or not
     */
    private Boolean success;
    /**
     * username for the user
     */
    private String userName;

    /**
     * default constructor
     */
    public RegisterResponse() {
    }

    /**
     * constructor for RegisterResponse
     * @param authToken AuthToken for the user
     * @param personID identifier for the user
     * @param success Tells if completed successfully or not
     * @param userName username for the user
     */
    public RegisterResponse(String authToken, String userName, String personID, Boolean success) {
        this.authToken = authToken;
        this.personID = personID;
        this.success = success;
        this.userName = userName;
    }


    /**
     * Constructor for reporting errors
     * @param message Tells success/error messages
     * @param success Tells if completed successfully or not
     */
    public RegisterResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }


    /**
     * Helper method to get the response body
     * @return JSON data is returned to handler in a String
     */
    @Override
    public String getResponseBody(){
        return new JsonSerializer().serialize(this, RegisterResponse.class);
    }



    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
