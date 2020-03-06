package Dao;

import fms.Dao.AuthTokenDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDaoTest {

    private Database db;
    private AuthToken bestAuthToken;

    @BeforeEach
    void setUp() {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new person with random data
        bestAuthToken = new AuthToken("123", "myUsername");
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }


    @Test
    void addPass() throws DataAccessException {//also get userName(authToken)
        String test = null;
        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            atDao.addAuthToken(bestAuthToken);
            test = atDao.getUserName(bestAuthToken.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNotNull(test);
    }



    @Test
    void insertPass() throws DataAccessException {//addAuthToken() {
        //We want to make sure addAuthToken works
        //First lets create an AuthToken that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        AuthToken compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            //While addAuthToken returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            atDao.addAuthToken(bestAuthToken);
            //So lets use a find method to get the event that we just put in back out
            compareTest = atDao.getAuthToken(bestAuthToken.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assertions.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our addAuthToken did put something in, and that it didn't change the
        //data in any way
        Assertions.assertEquals(bestAuthToken.getAuthToken(), compareTest.getAuthToken());
        Assertions.assertEquals(bestAuthToken.getUserName(), compareTest.getUserName());
    }

    @Test
    void insertFail() throws DataAccessException {//addAuthToken() {
        //lets do this test again but this time lets try to make it fail

        // NOTE: The correct way to test for an exception in Junit 5 is to use an assertThrows
        // with a lambda function. However, lambda functions are beyond the scope of this class
        // so we are doing it another way.
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            //if we call the method the first time it will addAuthToken it successfully
            atDao.addAuthToken(bestAuthToken);
            //but our sql table is set up so that "userName" must be unique. So trying to addAuthToken it
            //again will cause the method to throw an exception
            atDao.addAuthToken(bestAuthToken);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        Assertions.assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of addAuthToken should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        AuthToken compareTest = bestAuthToken;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = atDao.getAuthToken(bestAuthToken.getUserName());
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
        //First lets create an AuthToken that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        AuthToken compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            //While addAuthToken(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            atDao.addAuthToken(bestAuthToken);
            atDao.clear();
            compareTest = atDao.getAuthToken(bestAuthToken.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(compareTest);
    }

    @Test
    void getAuthTokenPass() throws DataAccessException {
        //We want to make sure clear works
        //First lets create an AuthToken that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        AuthToken compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            //While addAuthToken(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            atDao.addAuthToken(bestAuthToken);
            compareTest = atDao.getAuthToken(bestAuthToken.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNotNull(compareTest);
    }

    @Test
    void getAuthTokenFail() throws DataAccessException {
        //We want to make sure clear works
        //First lets create an AuthToken that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        AuthToken compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            //While addAuthToken(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            atDao.addAuthToken(bestAuthToken);
            String fakeUserName = "imafakeusername";
            compareTest = atDao.getAuthToken(fakeUserName);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(compareTest);
    }



    @Test
    void generateAuthTokenPass() throws DataAccessException {
        String auth1 = null;
        String auth2 = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            auth1 = atDao.generateAuthToken();
            auth2 = atDao.generateAuthToken();
            db.closeConnection(false);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    Assertions.assertNotNull(auth1);
    Assertions.assertNotNull(auth2);
    Assertions.assertNotEquals(auth1, auth2);
    }

    @Test
    void getUserNamePass() throws DataAccessException {
        String resultUserName = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            atDao.addAuthToken(bestAuthToken);
            resultUserName = atDao.getUserName(bestAuthToken.getAuthToken());
            db.closeConnection(false);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNotNull(resultUserName);
        Assertions.assertEquals(resultUserName, bestAuthToken.getUserName());
    }
    @Test
    void getUserNameFail() throws DataAccessException {
        String resultUserName = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao atDao = new AuthTokenDao(db);
            atDao.addAuthToken(bestAuthToken);
            resultUserName = atDao.getUserName("FakeAuthToken");
            db.closeConnection(false);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(resultUserName);
    }


    @Test
    void deleteAuthTokenPass() throws DataAccessException {
        AuthTokenDao atDao;
        AuthToken testAuthToken = null;
        try{
            Connection conn = db.openConnection();
            atDao = new AuthTokenDao(db);
            atDao.addAuthToken(bestAuthToken);
            atDao.deleteAuthToken(bestAuthToken.getAuthToken());
            testAuthToken = atDao.getAuthToken(bestAuthToken.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(testAuthToken);
    }
    @Test
    void deleteAuthTokenFail() throws DataAccessException {
        AuthTokenDao atDao;
        AuthToken testAuthToken = bestAuthToken;
        Connection conn = db.openConnection();
        atDao = new AuthTokenDao(db);
        try{
            Assertions.assertNotNull(testAuthToken);
            atDao.addAuthToken(bestAuthToken);
            //deleting a person that doesn't exist will throw an error we can catch
            atDao.deleteAuthToken("fakeID");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //The original person we added should still be there
            testAuthToken = atDao.getAuthToken(bestAuthToken.getUserName());
            db.closeConnection(false);
        }
        Assertions.assertNotNull(testAuthToken);
    }





}