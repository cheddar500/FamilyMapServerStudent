package Dao;

import fms.Dao.EventDao;
import fms.Exceptions.DataAccessException;
import fms.Database;
import fms.Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

class EventDaoTest {

    private Database db;
    private Event bestEvent;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
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
    public void insertPass() throws Exception {
        //We want to make sure addEvent works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Event compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //While addEvent returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.addEvent(bestEvent);
            //So lets use a find method to get the event that we just put in back out
            compareTest = eDao.getEvent(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assertions.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our addEvent did put something in, and that it didn't change the
        //data in any way
        Assertions.assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        //lets do this test again but this time lets try to make it fail

        // NOTE: The correct way to test for an exception in Junit 5 is to use an assertThrows
        // with a lambda function. However, lambda functions are beyond the scope of this class
        // so we are doing it another way.
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //if we call the method the first time it will addEvent it successfully
            eDao.addEvent(bestEvent);
            //but our sql table is set up so that "eventID" must be unique. So trying to addEvent it
            //again will cause the method to throw an exception
            eDao.addEvent(bestEvent);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        Assertions.assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of addEvent should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        Event compareTest = bestEvent;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = eDao.getEvent(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        Assertions.assertNull(compareTest);
    }


    @org.junit.jupiter.api.Test
    void clear() throws Exception {
        //We want to make sure clear works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Event compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //While addEvent returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.addEvent(bestEvent);
            eDao.clear();
            compareTest = eDao.getEvent(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assertions.assertNull(compareTest);;
    }

    @org.junit.jupiter.api.Test
    void getEventPass() throws Exception {
        //We want to make sure clear works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Event compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //While addEvent(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.addEvent(bestEvent);
            compareTest = eDao.getEvent(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNotNull(compareTest);
    }
    @org.junit.jupiter.api.Test
    void getEventFail() throws Exception {
        //We want to make sure clear works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Event compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //While addEvent(); returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.addEvent(bestEvent);
            String fakeEventID = "imafakeid";
            compareTest = eDao.getEvent(fakeEventID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(compareTest);
    }



    @Test
    void addAllEventsPass() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event p1 = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p2 = new Event("Biking_123B", "Bob", "Bob123B",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p3 = new Event("Biking_123C", "David", "David123C",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        eventList.add(p1); eventList.add(p2); eventList.add(p3);
        Event test1 = null; Event test2 = null; Event test3 = null;
        try{
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.addAllEvents(eventList);
            test1 = eDao.getEvent("Biking_123A");
            test2 = eDao.getEvent("Biking_123B");
            test3 = eDao.getEvent("Biking_123C");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertEquals(p1, test1);
        Assertions.assertEquals(p2, test2);
        Assertions.assertEquals(p3, test3);
    }

    @Test
    void addAllEventsFail() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event p1 = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p2 = new Event("Biking_123B", "Bob", "Bob123B",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p3 = new Event("Biking_123C", "David", "David123C",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        eventList.add(p1); eventList.add(p2); eventList.add(p3);
        Event test1 = null; Event test2 = null; Event test3 = null;
        try{
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.addEvent(p1); eDao.addEvent(p2); eDao.addEvent(p3);
            //Now trying to add all duplicates
            eDao.addAllEvents(eventList);
            test1 = eDao.getEvent(eventList.get(0).getEventID());
            test2 = eDao.getEvent(eventList.get(1).getEventID());
            test3 = eDao.getEvent(eventList.get(2).getEventID());
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
    void deleteEventPass() throws DataAccessException {
        EventDao eDao;
        Event testMan = null;
        try{
            Connection conn = db.openConnection();
            eDao = new EventDao(conn);
            eDao.addEvent(bestEvent);
            eDao.deleteEvent(bestEvent.getEventID());
            testMan = eDao.getEvent(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(testMan);
    }
    @Test
    void deleteEventFail() throws DataAccessException {
        EventDao eDao;
        Event testMan = bestEvent;
        Connection conn = db.openConnection();
        eDao = new EventDao(conn);
        try{
            Assertions.assertNotNull(testMan);
            eDao.addEvent(bestEvent);
            //deleting a person that doesn't exist will throw an error we can catch
            eDao.deleteEvent("fakeID");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //The original person we added should still be there
            testMan = eDao.getEvent(bestEvent.getEventID());
            db.closeConnection(false);
        }
        Assertions.assertNotNull(testMan);
    }

    @Test
    void deleteAllEventsPass() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event p1 = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p2 = new Event("Biking_123B", "Bob", "Bob123B",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p3 = new Event("Biking_123C", "David", "David123C",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        eventList.add(p1); eventList.add(p2); eventList.add(p3);
        Event test1 = null; Event test2 = null; Event test3 = null;
        try{
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.addAllEvents(eventList);
            eDao.deleteAllEvents(eventList);
            test1 = eDao.getEvent("id1");
            test2 = eDao.getEvent("id2");
            test3 = eDao.getEvent("id3");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        Assertions.assertNull(test1);
        Assertions.assertNull(test2);
        Assertions.assertNull(test3);
    }
    @Test
    void deleteAllEventsFail() throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event p1 = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p2 = new Event("Biking_123B", "Bob", "Bob123B",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p3 = new Event("Biking_123C", "David", "David123C",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        //only add two of the test people
        eventList.add(p1); eventList.add(p2);
        Event test1 = null; Event test2 = null; Event test3 = null;
        Connection conn = db.openConnection();
        EventDao eDao = new EventDao(conn);
        try{
            eDao.addAllEvents(eventList);
            //try to delete two people in the database and one who isn't
            eDao.deleteAllEvents(eventList);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //the people who were in the database should not be null because changes are rolled back
            Assertions.assertNotNull(eDao.getEvent(p1.getEventID()));
            Assertions.assertNotNull(eDao.getEvent(p2.getEventID()));
            db.closeConnection(false);
        }
    }



    @Test
    void getEventsOfPass() throws DataAccessException {
        Connection conn = db.openConnection();
        EventDao eDao = new EventDao(conn);
        Event p1 = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p3 = new Event("Biking_123C", "Gale", "David123C",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        ArrayList<Event> testPersons = new ArrayList<>();
        testPersons.add(p1); testPersons.add(p3);
        ArrayList<Event> comparePersons = new ArrayList<>();
        try {
            eDao.addEvent(p1);
            eDao.addEvent(p3);
            comparePersons = eDao.getEventsOf(p1.getUsername());
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
    void getEventsOfFail() throws DataAccessException {
        Connection conn = db.openConnection();
        EventDao eDao = new EventDao(conn);
        Event p1 = new Event("Biking_123A", "David", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Event p3 = new Event("Biking_123C", "David", "David123C",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        ArrayList<Event> comparePersons = new ArrayList<>();
        comparePersons.add(p1); comparePersons.add(p3);
        try {
            Assertions.assertNotNull(comparePersons);
            //try to get someone not in the database
            comparePersons = eDao.getEventsOf("fakeUsername");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(true);
            System.out.println("Failed :O");
        }
        Assertions.assertNull(comparePersons);
    }



}