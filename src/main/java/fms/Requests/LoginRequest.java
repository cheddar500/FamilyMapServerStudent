package fms.Requests;

/**
 * LoginRequest class lets LoginHandler send info to the LoginService
 */
public class LoginRequest {
    /**
     * password for the user logging in
     */
    private String password;
    /**
     * username for user logging in
     */
    private String userName;

    /**
     * default LoginRequest constructor
     */
    public LoginRequest(){
    }

    /**
     * Constructor to initialize all the variables for LoginRequest
     * @param password password for the user logging in
     * @param userName username for user logging in
     */
    public LoginRequest(String password, String userName) {
        this.password = password;
        this.userName = userName;
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
