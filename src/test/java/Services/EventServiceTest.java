package Services;

import fms.Dao.AuthTokenDao;
import fms.Dao.EventDao;
import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.AuthToken;
import fms.Model.Event;
import fms.Model.User;
import fms.Requests.EventRequest;
import fms.Requests.LoginRequest;
import fms.Responses.EventResponse;
import fms.Responses.LoginResponse;
import fms.Services.EventService;
import fms.Services.LoginService;
import org.junit.jupiter.api.*;


import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    private Database db;
    private Event testEvent1;
    private Event testEvent2;
    private Event testEvent3;
    private ArrayList<Event> testEvents;
    private User associatedUser;
    private String authToken;

    @BeforeEach
    public void setUp() throws Exception {
        //establish database connection
        db = new Database();
        Connection conn = db.openConnection();
        //create events
        testEvent1 = new Event("1mission", "taylor", "id1",11,12,
                "Cambodia", "Phnom Penh", "mission", 2016);
        testEvent2 = new Event("2vaca", "taylor", "id2",50,100,
                "Thailand", "Bangkok", "spirit vacation", 1997);
        testEvent3 = new Event("3woah", "ethington", "id3",154,27,
                "USA", "Provo", "admiring early america", 1778);
        testEvents = new ArrayList<>();
        try {
            db.clearTables();
            testEvents.add(testEvent1);
            testEvents.add(testEvent3);
            EventDao eventDao = new EventDao(conn);
            eventDao.addAllEvents(testEvents);
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
    void getOneEventPass() throws DataAccessException {
        boolean success = false;
        try{
            String eventID = "1mission"; //get the first of our test events
            boolean getAll = false; //just get one event
            EventRequest request = new EventRequest(authToken, eventID, getAll);
            EventService service = new EventService();
            EventResponse response = service.getEvent(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }
    @Test
    void getOneEventFailAuth(){
        boolean success = false;
        try{
            String eventID = "1mission"; //get the first of our test events
            authToken = "falseToken";//but use a bad authToken
            boolean getAll = false; //just get one event
            EventRequest request = new EventRequest(authToken, eventID, getAll);
            EventService service = new EventService();
            EventResponse response = service.getEvent(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }
//    @Test
//    @Disabled
//    void getOneEventFailID(){
//        boolean success = false;
//        try{
//            String eventID = "fakeID"; //get an event that doesn't exist
//            boolean getAll = false; //just get one event
//            EventRequest request = new EventRequest(authToken, eventID, getAll);
//            EventService service = new EventService();
//            EventResponse response = service.getEvent(request);
//            success = response.getSuccess();
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//        }
//        Assertions.assertFalse(success);
//    }
    @Test
    void getOneEventFailUser(){
        boolean success = false;
        try{
            String eventID = "3woah"; //get an event that doesn't belong to the user
            boolean getAll = false; //just get one event
            EventRequest request = new EventRequest(authToken, eventID, getAll);
            EventService service = new EventService();
            EventResponse response = service.getEvent(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }

    @Test
    void getManyEventsPass(){
        boolean success = false;
        try{
            String eventID = "1mission"; //pass in one eventID to verify belongs to requestee
            boolean getAll = true; //get more than one event
            EventRequest request = new EventRequest(authToken, eventID, getAll);
            EventService service = new EventService();
            EventResponse response = service.getEvent(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }
    @Test
    void getManyEventsFail(){
        boolean success = false;
        try{
            String eventID = "3woah"; //pass in one eventID that doesn't belong to requestee
            boolean getAll = true; //get more than one event
            EventRequest request = new EventRequest(authToken, eventID, getAll);
            EventService service = new EventService();
            EventResponse response = service.getEvent(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }

}