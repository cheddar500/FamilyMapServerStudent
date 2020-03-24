package Dao;

import fms.Dao.UserDao;
import fms.Exceptions.DataAccessException;
import fms.Database;
import fms.Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

class UserDaoTest {

    private Database db;
    private User bestUser;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new person with random data
        bestUser = new User("myUsername", "myPassword", "myEmail", "Chet",
                "Taylor", "m", "id321");
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    void insertPass() throws DataAccessException {
        //We want to make sure addUser works
        //First lets create an User that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        User compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //While addUser returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            uDao.addUser(bestUser);
            //So lets use a find method to get the event that we just put in back out
            compareTest = uDao.getUser(bestUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assertions.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our addUser did put something in, and that it didn't change the
        //data in any way
        Assertions.assertEquals(bestUser, compareTest);
    }
    @Test
    void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail

        // NOTE: The correct way to test for an exception in Junit 5 is to use an assertThrows
        // with a lambda function. However, lambda functions are beyond the scope of this class
        // so we are doing it another way.
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //if we call the method the first time it will addUser it successfully
            uDao.addUser(bestUser);
            //but our sql table is set up so that "eventID" must be unique. So trying to addUser it
            //again will cause the method to throw an exception
            uDao.addUser(bestUser);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        Assertions.assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of addUser should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        User compareTest = bestUser;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = uDao.getUser(bestUser.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        Assertions.assertNull(compareTest);
    }

    @Test
    void clear() throws DataAccessException {
        //We want to make sure clear works
        //First lets create an User that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        User compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //While addUser(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            uDao.addUser(bestUser);
            uDao.clear();
            compareTest = uDao.getUser(bestUser.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(compareTest);
    }

    @Test
    void getUserPass() throws DataAccessException {
        //We want to make sure clear works
        //First lets create an User that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        User compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //While addUser(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            uDao.addUser(bestUser);
            compareTest = uDao.getUser(bestUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNotNull(compareTest);
    }
    @Test
    void getUserFail() throws DataAccessException {
        //We want to make sure clear works
        //First lets create an User that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        User compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //While addUser(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            uDao.addUser(bestUser);
            String fakePersonID = "imafakeid";
            compareTest = uDao.getUser(fakePersonID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(compareTest);
    }
}