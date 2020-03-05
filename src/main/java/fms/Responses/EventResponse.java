package fms.Responses;

import fms.JsonSerializer;
import fms.Model.Event;

import java.util.ArrayList;

/**
 * Class with info about the Response of an EventRequest
 */
public class EventResponse implements IResponse{

    /**
     * City: City in which event occurred
     */
    private String city;
    /**
     * Country: Country in which event occurred
     */
    private String country;
    /**
     * Associated Username: User (Username) to which this person belongs
     */
    private String username;
    /**
     * Event ID: Unique identifier for this event (non-empty string)
     */
    private String eventID;
    /**
     * EventType: Type of event (birth, baptism, christening, marriage, death, etc.)
     */
    private String eventType;
    /**
     * Latitude: Latitude of event’s location
     */
    private Float latitude;
    /**
     * Longitude: Longitude of event’s location
     */
    private Float longitude;
    /**
     * Person ID: ID of person to which this event belongs
     */
    private String personID;
    /**
     * Year: Year in which event occurred
     */
    private Integer year;
    /**
     * Contains data for all events for this person
     */
    private ArrayList<Event> data;
    /**
     * Tells success/error messages
     */
    private String message;
    /**
     * Tells if request was successful or not
     */
    private Boolean success;

    /**
     * default constructor
     */
    public EventResponse() {
    }

    /**
     * constructor for one Event
     * @param city City in which event occurred
     * @param country Country in which event occurred
     * @param username User (Username) to which this person belongs
     * @param eventID Unique identifier for this event (non-empty string)
     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param latitude Latitude of event’s location
     * @param longitude longitude of event’s location
     * @param personID ID of person to which this event belongs
     * @param year Year in which event occurred
     * @param message Tells success/error messages
     * @param success Tells if request was successful or not
     */
    public EventResponse(String city, String country, String username, String eventID, String eventType, float latitude, float longitude, String personID, int year, String message, Boolean success) {
        this.city = city;
        this.country = country;
        this.username = username;
        this.eventID = eventID;
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.personID = personID;
        this.year = year;
        this.message = message;
        this.success = success;
    }

    /**
     * constructor for many Events
     * @param data Contains data for all events for this person
     * @param message Tells success/error messages
     * @param success Tells if completed successfully or not
     */
    public EventResponse(ArrayList<Event> data, String message, Boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    /**
     * Constructor for reporting errors
     * @param message Tells success/error messages
     * @param success Tells if completed successfully or not
     */
    public EventResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }



    /**
     * Helper method to get the response body
     * @return JSON data is returned to handler in a String
     */
    @Override
    public String getResponseBody(){
        return JsonSerializer.serialize(this, EventResponse.class);
    }







    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
