package fms.Services;

import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Requests.RegisterRequest;
import fms.Responses.RegisterResponse;

import java.sql.Connection;

/**
 * Login Register JSON Request and also completes the Register request
 * by using the Dao package classes
 */
public class RegisterService {

    /**
     * default constructor
     */
    public RegisterService() {
    }

    /**
     * Takes care of registering new users in the database
     * @param request All the info about the request
     * @return RegisterResponse object with info about the request
     */
    public RegisterResponse register(RegisterRequest request) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        //request contains:
//        * @param email Users email
//        * @param firstName Users first name
//        * @param lastName Users last name
//        * @param gender Users gender
//        * @param password Users password
//        * @param userName Users username
        //make user and store in database
        UserDao uDao = new UserDao(conn);
        //user:
//        * @param userName UserName: Unique user name (non-empty string)
//        * @param password Password: User’s password (non-empty string)
//        * @param email Email: User’s email address (non-empty string)
//        * @param firstName First Name: User’s first name (non-empty string)
//        * @param lastName Last Name: User’s last name (non-empty string)
//        * @param gender Gender: User’s gender (string: f or m)
//        * @param personID Person ID: Unique Person ID assigned to this user’s generated Person object -

        db.closeConnection(true);
        String message = "Successfully registered the new user";
//        return new RegisterResponse(message, true, personList);
        return null;
    }
}
