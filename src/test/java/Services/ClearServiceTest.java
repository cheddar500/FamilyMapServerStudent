package Services;

import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.User;
import fms.Responses.ClearResponse;
import fms.Services.ClearService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    //use before each to put stuff in database such as login

    @Test
    void clearPass() throws DataAccessException {
        //make a request if applicable (not clear)
        Database db = new Database();
        Connection conn = db.openConnection();
        UserDao uDao = new UserDao(conn);
        User user = new User("un","pw","em@me","fn","ln","m","pID");
        ClearService clear = new ClearService();
        ClearResponse clearResponse = clear.clear();
        Assertions.assertEquals(clearResponse.getMessage(),"Clear succeeded");
        //try to create UserDaos and get information, make sure null
        User postUser = uDao.getUser("un");
        db.closeConnection(true);
    }



}