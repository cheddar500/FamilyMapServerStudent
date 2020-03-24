package Services;

import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.User;
import fms.Requests.FillRequest;
import fms.Requests.LoginRequest;
import fms.Responses.FillResponse;
import fms.Responses.LoginResponse;
import fms.Services.FillService;
import fms.Services.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;



class FillServiceTest {

    private Database db;
    String username;

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
            LoginService service = new LoginService();
            LoginRequest request = new LoginRequest("pw",associatedUser.getUserName());
            LoginResponse response = service.login(request);
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
    public void fillPass() throws DataAccessException, SQLException, IOException {
        boolean success = false;
        try{
            int generations = 2;
            FillRequest request = new FillRequest(generations, username);
            FillResponse response = new FillService().fill(request);
            success = response.getSuccess();
        } catch (DataAccessException e){
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }

    @Test
    public void fillPassZero() throws SQLException, IOException {
        boolean success = false;
        try{
            int generations = 0;
            FillRequest request = new FillRequest(generations, username);
            FillResponse response = new FillService().fill(request);
            success = response.getSuccess();
        } catch (DataAccessException e){
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }

    @Test
    public void fillFailUsername(){
        boolean success = false;
        try{
            int generations = 0;
            username = "fakeusern@me";//try with bad username
            FillRequest request = new FillRequest(generations, username);
            FillResponse response = new FillService().fill(request);
            success = response.getSuccess();
        } catch (DataAccessException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }

    @Test
    public void fillFailGenNum(){
        boolean success = false;
        try{
            int generations = -1; //try with invalid generation number
            FillRequest request = new FillRequest(generations, username);
            FillResponse response = new FillService().fill(request);
            success = response.getSuccess();
        } catch (DataAccessException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }


}