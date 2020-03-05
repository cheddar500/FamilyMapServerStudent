package fms.Services;

import fms.Dao.AuthTokenDao;
import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.AuthToken;
import fms.Model.User;
import fms.Requests.LoginRequest;
import fms.Responses.LoginResponse;

import java.sql.Connection;

/**
 * Login interprets JSON Request and also completes the Login request
 * by using the Dao package classes
 */
public class LoginService {

    /**
     * default constructor
     */
    public LoginService() {
    }

    /**
     * Takes care of logging the user into the database
     * @return Returns LoginResponse object reporting success/fail
     */
    public LoginResponse login(LoginRequest request) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        UserDao uDao = new UserDao(conn);
        User user = uDao.login(request.getUserName(), request.getPassword());
        //check validity of login
        if(user == null || request.getUserName() == null || request.getPassword() == null){
            db.closeConnection(true);
            return new LoginResponse("User/Password combo doesn't exist", false);
        }
        //login seems valid, try to get authToken
        AuthTokenDao atDao = new AuthTokenDao(conn);
        AuthToken authTokenClass = atDao.getAuthToken(request.getUserName());
        //check if username doesn't have an authToken
        if(authTokenClass == null){
            String newAuthToken = new AuthTokenDao(conn).generateAuthToken();
            atDao.addAuthToken(new AuthToken(newAuthToken, request.getUserName()));
            authTokenClass = atDao.getAuthToken(request.getUserName());
        }
        String message = "Successfully logged in";
        db.closeConnection(true);
        return new LoginResponse(authTokenClass.getAuthToken(), message, user.getPersonID(), true, authTokenClass.getUserName());
    }
}
