package Services;

import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.User;
import fms.Requests.LoginRequest;
import fms.Responses.LoginResponse;
import fms.Services.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {


    private Database db;
    private String username;

    @BeforeEach
    public void setUp() throws Exception {
        //establish database connection
        db = new Database();
        Connection conn = db.openConnection();

        try {
            db.clearTables();
            UserDao userDao = new UserDao(conn);
            username = "taylor";
            User associatedUser = new User("taylor", "pw",
                    "email", "Chet", "Taylor",
                    "m", "1me");
            userDao.addUser(associatedUser);
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
    public void loginPass(){
        boolean success = false;
        try {
            LoginService service = new LoginService();
            LoginRequest request = new LoginRequest("pw",username);
            LoginResponse response = service.login(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }

    @Test
    public void loginFailUsername(){
        boolean success = false;
        try {
            username = "fakeUsern@mae";
            LoginService service = new LoginService();
            LoginRequest request = new LoginRequest("pw",username);
            LoginResponse response = service.login(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }
    @Test
    public void loginFailPassword(){
        boolean success = false;
        try {
            LoginService service = new LoginService();
            LoginRequest request = new LoginRequest("f@keN3w5",username);
            LoginResponse response = service.login(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }

}