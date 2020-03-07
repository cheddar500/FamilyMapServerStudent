package Services;

import fms.Dao.AuthTokenDao;
import fms.Dao.PersonDao;
import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.AuthToken;
import fms.Model.Person;
import fms.Model.User;
import fms.Requests.PersonRequest;
import fms.Requests.LoginRequest;
import fms.Responses.PersonResponse;
import fms.Responses.LoginResponse;
import fms.Services.PersonService;
import fms.Services.LoginService;
import org.junit.jupiter.api.*;


import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    private Database db;
    private Person testPerson1;
    private Person testPerson2;
    private Person testPerson3;
    private ArrayList<Person> testPersons;
    private User associatedUser;
    private String authToken;

    @BeforeEach
    public void setUp() throws Exception {
        //establish database connection
        db = new Database();
        Connection conn = db.openConnection();
        //create persons
        testPerson1 = new Person("ID1", "taylor", "chet",
                "taylor","m", "fID1", "mID1", "ID2");
        testPerson2 = new Person("ID2", "taylor", "emily",
                "taylor","f", "fID2", "mID2", "ID1");
        testPerson3 = new Person("ID3", "who", "dont",
                "care","m", "fID3", null, null);
        testPersons = new ArrayList<>();
        try {
            db.clearTables();
            testPersons.add(testPerson1);
            testPersons.add(testPerson3);
            PersonDao personDao = new PersonDao(conn);
            personDao.addAllPersons(testPersons);
            //login associated user
            UserDao userDao = new UserDao(conn);
            associatedUser = new User("taylor", "pw",
                    "email", "Chet", "Taylor",
                    "m", "1me");
            userDao.addUser(associatedUser);
            db.closeConnection(true);
            LoginService service = new LoginService();
            LoginRequest request = new LoginRequest("pw",associatedUser.getUserName());
            LoginResponse response = service.login(request);
            conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(conn);
            AuthToken authTokenBig = atDao.getAuthToken(associatedUser.getUserName());
            authToken = authTokenBig.getAuthToken();
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
    void getOnePersonPass() throws DataAccessException {
        boolean success = false;
        try{
            String personID = "ID1"; //get the first of our test events
            boolean getAll = false; //just get one event
            PersonRequest request = new PersonRequest(authToken, personID, getAll);
            PersonService service = new PersonService();
            PersonResponse response = service.getPerson(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }
    @Test
    void getOnePersonFailAuth(){
        boolean success = false;
        try{
            String personID = "ID1"; //get the first of our test events
            authToken = "falseToken";//but use a bad authToken
            boolean getAll = false; //just get one event
            PersonRequest request = new PersonRequest(authToken, personID, getAll);
            PersonService service = new PersonService();
            PersonResponse response = service.getPerson(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }
//    @Test
//    @Disabled
//    void getOnePersonFailID(){
//        boolean success = false;
//        try{
//            String personID = "fakeID"; //get an event that doesn't exist
//            boolean getAll = false; //just get one event
//            PersonRequest request = new PersonRequest(authToken, personID, getAll);
//            PersonService service = new PersonService();
//            PersonResponse response = service.getPerson(request);
//            success = response.getSuccess();
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//        }
//        Assertions.assertFalse(success);
//    }
    @Test
    void getOnePersonFailUser(){
        boolean success = false;
        try{
            String personID = "ID3"; //get an event that doesn't belong to the user
            boolean getAll = false; //just get one event
            PersonRequest request = new PersonRequest(authToken, personID, getAll);
            PersonService service = new PersonService();
            PersonResponse response = service.getPerson(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }

    @Test
    void getManyPersonsPass(){
        boolean success = false;
        try{
            String personID = "ID1"; //pass in one personID to verify belongs to requestee
            boolean getAll = true; //get more than one event
            PersonRequest request = new PersonRequest(authToken, personID, getAll);
            PersonService service = new PersonService();
            PersonResponse response = service.getPerson(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }
    @Test
    void getManyPersonsFail(){
        boolean success = false;
        try{
            String personID = "ID3"; //pass in one personID that doesn't belong to requestee
            boolean getAll = true; //get more than one event
            PersonRequest request = new PersonRequest(authToken, personID, getAll);
            PersonService service = new PersonService();
            PersonResponse response = service.getPerson(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }

}