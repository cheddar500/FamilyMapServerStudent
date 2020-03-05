package fms.Model;

import java.util.Objects;

/**
 * User class to represent users in the database
 */
public class User {


    /**
     * UserName: Unique user name (non-empty string)
     */
    private String userName;
    /**
     * Password: User’s password (non-empty string)
     */
    private String password;
    /**
     * Email: User’s email address (non-empty string)
     */
    private String email;
    /**
     * First Name: User’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Last Name: User’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Gender: User’s gender (string: f or m)
     */
    private String gender;
    /**
     * Person ID: Unique Person ID assigned to this user’s generated Person object -
     * see Family History Information section for details (non-empty string)
     */
    private String personID;

    /**
     * default constructor for User
     */
    public User() {
    }

    /**
     * This is the constructor for the User class
     * @param userName UserName: Unique user name (non-empty string)
     * @param password Password: User’s password (non-empty string)
     * @param email Email: User’s email address (non-empty string)
     * @param firstName First Name: User’s first name (non-empty string)
     * @param lastName Last Name: User’s last name (non-empty string)
     * @param gender Gender: User’s gender (string: f or m)
     * @param personID Person ID: Unique Person ID assigned to this user’s generated Person object -
     *        see Family History Information section for details (non-empty string)
     */
    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }



    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
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


    public String getPersonID() {
        return personID;
    }


    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserName().equals(user.getUserName()) &&
                getPassword().equals(user.getPassword()) &&
                getEmail().equals(user.getEmail()) &&
                getFirstName().equals(user.getFirstName()) &&
                getLastName().equals(user.getLastName()) &&
                getGender().equals(user.getGender()) &&
                getPersonID().equals(user.getPersonID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getPassword(), getEmail(), getFirstName(), getLastName(), getGender(), getPersonID());
    }
}
