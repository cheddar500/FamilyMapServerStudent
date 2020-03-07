package Services;

import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.User;
import fms.Requests.LoginRequest;
import fms.Requests.RegisterRequest;
import fms.Responses.LoginResponse;
import fms.Responses.RegisterResponse;
import fms.Services.LoginService;
import fms.Services.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    private Database db;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String password;
    private String userName;

    @BeforeEach
    public void setUp() throws Exception {
        //establish database connection
        db = new Database();
        Connection conn = db.openConnection();

        try {
            db.openConnection();
            db.clearTables();
            email = "email";
            firstName = "firstName";
            lastName = "lastName";
            gender = "m";
            password = "password";
            userName = "userName";
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void registerPass(){
        boolean success = false;
        try{
            RegisterRequest request = new RegisterRequest(email,firstName,lastName,
                    gender,password,userName);
            RegisterResponse response = new RegisterService().register(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }

    @Test
    public void registerFailUsername(){
        boolean success = false;
        userName = null; //invalid request property
        try{
            RegisterRequest request = new RegisterRequest(userName,password,email,
                    firstName,lastName,gender);
            RegisterResponse response = new RegisterService().register(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }



}