package fms.Model;

/**
 * AuthToken class to represent authentication tokens in the database
 */
public class AuthToken {

    /**
     * Unique authorization token String for the user
     */
    private String authToken;
    /**
     * Identifier for a user
     */
    private String userName;

    /**
     * default constructor for AuthToken
     */
    public AuthToken(){
    }

    /**
     * This is the constructor for the AuthToken class
     * @param authToken Unique authorization token String for the user
     * @param userName Identifier for a user
     */
    public AuthToken(String authToken, String userName) {
        this.authToken = authToken;
        this.userName = userName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

