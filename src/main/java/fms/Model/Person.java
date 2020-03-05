package fms.Model;

import java.util.Objects;

/**
 * Person class to represent a person in the database
 */
public class Person {

    /**
     * Person ID: Unique identifier for this person (non-empty string)
     */
    private String personID;
    /**
     * Associated Username: User (Username) to which this person belongs
     */
    private String associatedUsername;
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
     * default constructor for Person
     */
    public Person() {
    }

    /**
     * This is the constructor for the Person class
     * @param personID Person ID: Unique identifier for this person (non-empty string)
     * @param associatedUsername Associated Username: User (Username) to which this person belongs
     * @param firstName First Name: Person’s first name (non-empty string)
     * @param lastName Last Name: Person’s last name (non-empty string)
     * @param gender Gender: Person’s gender (string: f or m)
     * @param fatherID Father ID: Person ID of person’s father (possibly null)
     * @param motherID Mother ID: Person ID of person’s mother (possibly null)
     * @param spouseID Spouse ID: Person ID of person’s spouse (possibly null)
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getPersonID().equals(person.getPersonID()) &&
                getUsername().equals(person.getUsername()) &&
                getFirstName().equals(person.getFirstName()) &&
                getLastName().equals(person.getLastName()) &&
                getGender().equals(person.getGender()) &&
                Objects.equals(getFatherID(), person.getFatherID()) &&
                Objects.equals(getMotherID(), person.getMotherID()) &&
                Objects.equals(getSpouseID(), person.getSpouseID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonID(), getUsername(), getFirstName(), getLastName(), getGender(), getFatherID(), getMotherID(), getSpouseID());
    }
}
