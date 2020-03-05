package Dao;

import fms.Dao.PersonDao;
import fms.Exceptions.DataAccessException;
import fms.Database;
import fms.Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

class PersonDaoTest {

    private Database db;
    private Person bestPerson;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new person with random data
        bestPerson = new Person("id123", "myUsername", "Chet",
                "Taylor", "m", "id321",
                "id222", "id111");
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
    void insertPass() throws Exception {
        //We want to make sure addPerson works
        //First lets create an Person that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Person compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //While addPerson returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            pDao.addPerson(bestPerson);
            //So lets use a find method to get the event that we just put in back out
            compareTest = pDao.getPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assertions.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our addPerson did put something in, and that it didn't change the
        //data in any way
        Assertions.assertEquals(bestPerson, compareTest);
    }
    @Test
    void insertFail() throws Exception {
        //lets do this test again but this time lets try to make it fail

        // NOTE: The correct way to test for an exception in Junit 5 is to use an assertThrows
        // with a lambda function. However, lambda functions are beyond the scope of this class
        // so we are doing it another way.
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //if we call the method the first time it will addPerson it successfully
            pDao.addPerson(bestPerson);
            //but our sql table is set up so that "eventID" must be unique. So trying to addPerson it
            //again will cause the method to throw an exception
            pDao.addPerson(bestPerson);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        Assertions.assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of addPerson should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        Person compareTest = bestPerson;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = pDao.getPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        Assertions.assertNull(compareTest);
    }

    @Test
    void clear() throws Exception {
        //We want to make sure clear works
        //First lets create an Person that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Person compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //While addPerson(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            pDao.addPerson(bestPerson);
            pDao.clear();
            compareTest = pDao.getPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(compareTest);
    }

    @Test
    void getPersonPass() throws Exception {
        //We want to make sure clear works
        //First lets create an Person that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Person compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //While addPerson(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            pDao.addPerson(bestPerson);
            compareTest = pDao.getPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNotNull(compareTest);
    }
    @Test
    void getPersonFail() throws Exception {
        //We want to make sure clear works
        //First lets create an Person that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Person compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //While addPerson(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            pDao.addPerson(bestPerson);
            String fakePersonID = "imafakeid";
            compareTest = pDao.getPerson(fakePersonID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(compareTest);
    }

    @Test
    void addAllPersonsPass() throws DataAccessException {
        List<Person> personList = new ArrayList<>();
        Person p1 = new Person("id1","un1","bob","marley","m","","","");
        Person p2 = new Person("id2","un2","bobby","fish","m","dean bill","lucy hi","");
        Person p3 = new Person("id3","un3","bird","flappy","m","doug","lindsey","mary");
        personList.add(p1); personList.add(p2); personList.add(p3);
        Person test1 = null; Person test2 = null; Person test3 = null;
        try{
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.addAllPersons(personList);
            test1 = pDao.getPerson("id1");
            test2 = pDao.getPerson("id2");
            test3 = pDao.getPerson("id3");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertEquals(p1, test1);
        Assertions.assertEquals(p2, test2);
        Assertions.assertEquals(p3, test3);
    }

    @Test
    void addAllPersonsFail() throws DataAccessException {
        List<Person> personList = new ArrayList<>();
        Person p1 = new Person("id1", "un1","bob","marley","m","","","");
        Person p2 = new Person("id2","un2","bobby","fish","m","dean bill","lucy hi","");
        Person p3 = new Person("id3","un3","bird","flappy","m","doug","lindsey","mary");
        personList.add(p1); personList.add(p2); personList.add(p3);
        Person test1 = null; Person test2 = null; Person test3 = null;
        try{
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.addPerson(p1); pDao.addPerson(p2); pDao.addPerson(p3);
            //Now trying to add all duplicates
            pDao.addAllPersons(personList);
            test1 = pDao.getPerson(personList.get(0).getPersonID());
            test2 = pDao.getPerson(personList.get(1).getPersonID());
            test3 = pDao.getPerson(personList.get(2).getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //All adding should have failed due to duplication
            Assertions.assertNull(test1);
            Assertions.assertNull(test2);
            Assertions.assertNull(test3);
            db.closeConnection(false);
        }
    }

    @Test
    void deletePersonPass() throws DataAccessException {
        PersonDao pDao;
        Person testMan = null;
        try{
            Connection conn = db.openConnection();
            pDao = new PersonDao(conn);
            pDao.addPerson(bestPerson);
            pDao.deletePerson(bestPerson.getPersonID());
            testMan = pDao.getPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(testMan);
    }
    @Test
    void deletePersonFail() throws DataAccessException {
        PersonDao pDao;
        Person testMan = bestPerson;
        Connection conn = db.openConnection();
        pDao = new PersonDao(conn);
        try{
            Assertions.assertNotNull(testMan);
            pDao.addPerson(bestPerson);
            //deleting a person that doesn't exist will throw an error we can catch
            pDao.deletePerson("fakeID");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //The original person we added should still be there
            testMan = pDao.getPerson(bestPerson.getPersonID());
            db.closeConnection(false);
        }
        Assertions.assertNotNull(testMan);
    }

    @Test
    void deleteAllPersonsPass() throws DataAccessException {
        List<Person> personList = new ArrayList<>();
        Person p1 = new Person("id1","un1","bob","marley","m","","","");
        Person p2 = new Person("id2","un2","bobby","fish","m","dean bill","lucy hi","");
        Person p3 = new Person("id3","un3","bird","flappy","m","doug","lindsey","mary");
        personList.add(p1); personList.add(p2); personList.add(p3);
        Person test1 = null; Person test2 = null; Person test3 = null;
        try{
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.addAllPersons(personList);
            pDao.deleteAllPersons(personList);
            test1 = pDao.getPerson("id1");
            test2 = pDao.getPerson("id2");
            test3 = pDao.getPerson("id3");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(test1);
        Assertions.assertNull(test2);
        Assertions.assertNull(test3);
    }
    @Test
    void deleteAllPersonsFail() throws DataAccessException {
        List<Person> personList = new ArrayList<>();
        Person p1 = new Person("id1","un1","bob","marley","m","","","");
        Person p2 = new Person("id2","un2","bobby","fish","m","dean bill","lucy hi","");
        Person p3 = new Person("id3","un3","bird","flappy","m","doug","lindsey","mary");
        //only add two of the test people
        personList.add(p1); personList.add(p2);
        Person test1 = null; Person test2 = null; Person test3 = null;
        Connection conn = db.openConnection();
        PersonDao pDao = new PersonDao(conn);
        try{
            pDao.addAllPersons(personList);
            //try to delete two people in the database and one who isn't
            pDao.deleteAllPersons(personList);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //the people who were in the database should not be null because changes are rolled back
            Assertions.assertNotNull(pDao.getPerson(p1.getPersonID()));
            Assertions.assertNotNull(pDao.getPerson(p2.getPersonID()));
            db.closeConnection(false);
        }
    }


    @Test
    void getPersonsOfPass() throws DataAccessException {
        Connection conn = db.openConnection();
        PersonDao pDao = new PersonDao(conn);
        Person p1 = new Person("id1","un1","bob","marley","m","","","");
        Person p2 = new Person("id2","un2","bobby","fish","m","dean bill","lucy hi","");
        Person p3 = new Person("id3","un1","bird","flappy","m","doug","lindsey","mary");
        ArrayList<Person> testPersons = new ArrayList<>();
        testPersons.add(p1); testPersons.add(p3);
        ArrayList<Person> comparePersons = new ArrayList<>();
        try {
            pDao.addPerson(p1);
            pDao.addPerson(p3);
            comparePersons = pDao.getPersonsOf(p1.getUsername());
            db.closeConnection(true);
            Assertions.assertNotNull(comparePersons);
        } catch (DataAccessException e) {
            db.closeConnection(true);
            System.out.println("Failed :O");
        }
        // This could fail depending on the order in the ArrayList that the persons are returned
        Assertions.assertEquals(testPersons, comparePersons);
    }
    @Test
    void getPersonsOfFail() throws DataAccessException {
        Connection conn = db.openConnection();
        PersonDao pDao = new PersonDao(conn);
        Person p1 = new Person("id1","un1","bob","marley","m","","","");
        Person p3 = new Person("id3","un1","bird","flappy","m","doug","lindsey","mary");
        ArrayList<Person> comparePersons = new ArrayList<>();
        comparePersons.add(p1); comparePersons.add(p3);
        try {
            Assertions.assertNotNull(comparePersons);
            //try to get someone not in the database
            comparePersons = pDao.getPersonsOf("fakeUsername");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(true);
            System.out.println("Failed :O");
        }
        Assertions.assertNull(comparePersons);
    }

}