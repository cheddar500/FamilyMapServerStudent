package fms.Requests;

/**
 * RegisterRequest class lets RegisterHandler send info to the RegisterService
 */
public class RegisterRequest {

    /**
     * Users email address
     */
    private String email;
    /**
     * Users first name
     */
    private String firstName;
    /**
     * Users last name
     */
    private String lastName;
    /**
     * Users gender
     */
    private String gender;
    /**
     * Users password
     */
    private String password;
    /**
     * Users password
     */
    private String userName;

    /**
     * default constructor
     */
    public RegisterRequest(){
    }

    /**
     * Constructor to initialize all the variables for RegisterRequest
     * @param email Users email
     * @param firstName Users first name
     * @param lastName Users last name
     * @param gender Users gender
     * @param password Users password
     * @param userName Users username
     */
    public RegisterRequest(String email, String firstName, String lastName, String gender, String password, String userName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.password = password;
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
