package fms.Requests;

/**
 * EventRequest class lets EventHandler send info to the EventService
 */
public class EventRequest {

    /**
     * Allows current User to access correct information
     */
    private String authToken;
    /**
     * Uniquely identifies requested Event
     */
    private String eventID;
    /**
     * A flag to tell if returning one or all of the User's Events
     */
    private Boolean getAll;

    /**
     * Default constructor
     */
    public EventRequest() {}

    /**
     * Constructor to initialize all the variables for EventRequest
     * @param authToken Allows current User to access correct information
     * @param eventID Uniquely identifies requested Event
     * @param getAll A flag to tell if returning one or all of the User's Events
     */
    public EventRequest(String authToken, String eventID, Boolean getAll) {
        this.authToken = authToken;
        this.eventID = eventID;
        this.getAll = getAll;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public Boolean getGetAll() {
        return getAll;
    }

    public void setGetAll(Boolean getAll) {
        this.getAll = getAll;
    }
}
