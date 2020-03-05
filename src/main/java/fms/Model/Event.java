package fms.Model;

/**
 * Event class to represent events in the database
 */
public class Event {

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
    private String associatedUsername;
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
    private double latitude;
    /**
     * Longitude: Longitude of event’s location
     */
    private double longitude;
    /**
     * Person ID: ID of person to which this event belongs
     */
    private String personID;
    /**
     * Year: Year in which event occurred
     */
    private int year;

    /**
     * default constructor for Event
     */
    public Event(){
    }

    /**
     * This is the constructor for the Event class
     * @param city City: City in which event occurred
     * @param country Country: Country in which event occurred
     * @param associatedUsername Associated Username: User (Username) to which this person belongs
     * @param eventID Event ID: Unique identifier for this event (non-empty string)
     * @param eventType EventType: Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param latitude Latitude: Latitude of event’s location
     * @param longitude Longitude: Longitude of event’s location
     * @param personID Person ID: ID of person to which this event belongs
     * @param year Year: Year in which event occurred
     */
    public Event(String eventID, String associatedUsername, String personID, double latitude, double longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
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
        return associatedUsername;
    }

    public void setUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getUsername().equals(getUsername()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear());
        } else {
            return false;
        }
    }
}
