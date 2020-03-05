package fms.Responses;

import fms.Model.Person;

import java.util.ArrayList;

/**
 * Class with info about the Response of a PersonAllRequest
 */
public class PersonAllResponse {

    /**
     * Person ID: Unique identifier for this person (non-empty string)
     */
    private String personID;
    /**
     * Associated Username: User (Username) to which this person belongs
     */
    private String userName;
    /**
     * First Name: Person’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Last Name: Person’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Gender: Person’s gender (string: f or m)
     */
    private String gender;
    /**
     * Father ID: Person ID of person’s father (possibly null)
     */
    private String fatherID;
    /**
     * Mother ID: Person ID of person’s mother (possibly null)
     */
    private String motherID;
    /**
     * Spouse ID: Person ID of person’s spouse (possibly null)
     */
    private String spouseID;
    /**
     * Contains data for all family members
     */
    private ArrayList<Person> data;
    /**
     * Tells success/error messages
     */
    private String message;
    /**
     * Tells if completed successfully or not
     */
    private Boolean success;

    /**
     * default constructor
     */
    public PersonAllResponse() {
    }

    /**
     * constructor for PersonAllResponse
     * @param personID Person ID: Unique identifier for this person (non-empty string)
     * @param userName Associated Username: User (Username) to which this person belongs
     * @param firstName First Name: Person’s first name (non-empty string)
     * @param lastName Last Name: Person’s last name (non-empty string)
     * @param gender Gender: Person’s gender (string: f or m)
     * @param fatherID Father ID: Person ID of person’s father (possibly null)
     * @param motherID Mother ID: Person ID of person’s mother (possibly null)
     * @param spouseID Spouse ID: Person ID of person’s spouse (possibly null)
     * @param data Contains data for all family members
     * @param message Tells success/error messages
     * @param success Tells if completed successfully or not
     */
    public PersonAllResponse(String personID, String userName, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID, ArrayList<Person> data, String message, Boolean success) {
        this.personID = personID;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
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
