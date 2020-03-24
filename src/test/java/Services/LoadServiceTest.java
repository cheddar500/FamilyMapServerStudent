package Services;

import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.Event;
import fms.Model.Person;
import fms.Model.User;
import fms.Requests.LoadRequest;
import fms.Responses.LoadResponse;
import fms.Services.LoadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.util.ArrayList;



class LoadServiceTest {

    private Database db;
    private ArrayList<Event> eventList;
    private ArrayList<Person> personList;
    private ArrayList<User> userList;

    @BeforeEach
    public void setUp() throws Exception {
        //establish database connection
        db = new Database();
        Connection conn = db.openConnection();

        try {
            db.clearTables();
            //generate data to test in loading
            eventList = new ArrayList<>();
            personList = new ArrayList<>();
            userList = new ArrayList<>();
            Event testEvent1 = new Event("1mission", "taylor", "id1",11,12,
                    "Cambodia", "Phnom Penh", "mission", 2016);
            Event testEvent2 = new Event("2vaca", "taylor", "id2",50,100,
                    "Thailand", "Bangkok", "spirit vacation", 1997);
            eventList.add(testEvent1); eventList.add(testEvent2);
            Person testPerson1 = new Person("ID3", "taylor", "chet",
                    "taylor","m", "fID1", "mID1", "ID2");
            Person testPerson2 = new Person("ID4", "taylor", "emily",
                    "taylor","f", "fID2", "mID2", "ID1");
            personList.add(testPerson1); personList.add(testPerson2);
            User user1 = new User("taylor", "pw",
                    "email", "Chet", "Taylor",
                    "m", "1me");
            User user2 = new User("taylor", "pw",
                    "email", "Chet", "Taylor",
                    "m", "1you");
            userList.add(user1); userList.add(user2);
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
    public void loadPass(){
        boolean success = false;
        try{
            LoadRequest request = new LoadRequest(userList,personList,eventList);
            LoadResponse response = new LoadService().load(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(success);
    }

    @Test
    public void loadFail(){
        boolean success = false;
        try{
            userList = null; //try passing in invalid data
            LoadRequest request = new LoadRequest(userList,personList,eventList);
            LoadResponse response = new LoadService().load(request);
            success = response.getSuccess();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(success);
    }

}