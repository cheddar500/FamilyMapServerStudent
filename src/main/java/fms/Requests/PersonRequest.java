package fms.Requests;

/**
 * PersonRequest class lets PersonHandler send info to the PersonService
 */
public class PersonRequest {

    /**
     * Allows current User to access correct information
     */
    private String authToken;
    /**
     * Uniquely identifies requested Ancestor (Person)
     */
    private String personID;
    /**
     * A flag to tell if returning one or all of the User's Ancestors
     */
    private Boolean getAll;

    /**
     * Default constructor
     */
    public PersonRequest() {
    }

    /**
     * Constructor to initialize all the variables for PersonRequest
     * @param authToken Allows current User to access correct information
     * @param personID Uniquely identifies requested Ancestor (Person)
     * @param getAll A flag to tell if returning one or all of the User's Ancestors
     */
    public PersonRequest(String authToken, String personID, Boolean getAll) {
        this.authToken = authToken;
        this.personID = personID;
        this.getAll = getAll;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Boolean getGetAll() {
        return getAll;
    }

    public void setGetAll(Boolean getAll) {
        this.getAll = getAll;
    }
}
