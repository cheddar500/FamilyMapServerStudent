package fms.Services;

import fms.Dao.AuthTokenDao;
import fms.Dao.EventDao;
import fms.Dao.PersonDao;
import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.Event;
import fms.Model.Person;
import fms.Model.User;
import fms.Requests.LoadRequest;
import fms.Responses.LoadResponse;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Load interprets JSON Request and also completes the Load request
 * by using the Dao package classes
 */
public class LoadService {
    /**
     * default constructor
     */
    public LoadService() {
    }

    /**
     * Loads Users/Persons/Events from provided JSON
     * @param request Has specifics of the Request
     * @return Returns LoadResponse object with info about Request
     */
    public LoadResponse load(LoadRequest request) throws DataAccessException {
        //Database connection
        Database db = new Database();
        //Dao objects
        UserDao uDao = new UserDao(db);
        EventDao eDao = new EventDao(db);
        PersonDao pDao = new PersonDao(db);
        //Clear out everything
        uDao.clear();
        eDao.clear();
        pDao.clear();
        new AuthTokenDao(db).clear();
        ArrayList<User> users = request.getUsers();
        ArrayList<Event> events = request.getEvents();
        ArrayList<Person> people = request.getPersons();
        for (int i = 0; i < users.size(); i++) {
            uDao.addUser(users.get(i));
        }
        eDao.addAllEvents(events);
        pDao.addAllPersons(people);
        int numberOfUsers = users.size();
        int numOfPersons = people.size();
        int numOfEvents = events.size();
        String message = "Successfully added "+numberOfUsers+" users, "+numOfPersons+" persons, and "+numOfEvents+" events to the database.";
        return new LoadResponse(message, true);
    }
}
