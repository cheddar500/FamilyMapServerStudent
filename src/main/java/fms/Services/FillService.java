package fms.Services;

import fms.Dao.EventDao;
import fms.Dao.PersonDao;
import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Generate;
import fms.Model.Event;
import fms.Model.Person;
import fms.Model.User;
import fms.Requests.FillRequest;
import fms.Responses.FillResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Fill interprets JSON Request and also completes the Fill request
 * by using the Dao package classes
 */
public class FillService {

    //******************************************************************************************************************
    /**
     * default constructor
     */
    public FillService() {
    }

    //******************************************************************************************************************
    /**
     * Populates the server's database with generated data for the specified user name
     * @param request Has specifics of the Request
     * @return Returns FillResponse object with info about Request
     * @throws DataAccessException
     */
    public FillResponse fill(FillRequest request) throws DataAccessException, IOException {
        Database db = new Database();
        Connection conn = db.openConnection();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //generations default 4, must be non-negative
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(request.getNumOfGenerations() == null){
            String message = "Error: invalid number of generations, must be non-negative";
            return new FillResponse(message, false);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //The required "username" parameter must be a user already registered with the server
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String userName = request.getUserName();
        UserDao uDao = new UserDao(conn);
        User user = uDao.getUser(userName);
        if(user == null){
            String message = "Error: invalid userName";
            return new FillResponse(message, false);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //If there is any data in the database already associated with the given user name, it is deleted
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //?????????check if null for data as well and throw error or always fine since already registered????***************************?????????????????
        deleteUserData(conn, userName);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////Generate data//////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////
        //A user’s family history data should include at least one Person object, which represents the user him or herself
        //Beyond the user’s own Person object, there can be zero or more additional generations of data.
        /////////////////////////////////////////////
        // Based on the requested number of generations, you should fill out the user’s family tree with generated Person and Event data.
//        addGeneratedFamilyToTree(conn, request, user);
        Generate getData = new Generate();
        getData.generateInfo(user, request.getNumOfGenerations(), conn);

        /////////////////////////////////////////////
        //Close connection, return response
        /////////////////////////////////////////////
        db.closeConnection(true);
        String message = "Successfully filled in data for the user";
        return new FillResponse(message, true);
    }

    //******************************************************************************************************************

    /**
     * Deletes existing user data
     * @param conn the connection to the database
     * @param userName the users username
     * @throws DataAccessException
     */
    private void deleteUserData(Connection conn, String userName) throws DataAccessException {
        ArrayList<Person> associatedPeople;
        ArrayList<Event> associatedEvents;
        PersonDao pDao = new PersonDao(conn);
        EventDao eDao = new EventDao(conn);
        associatedPeople = pDao.getPersonsOf(userName);
        associatedEvents = eDao.getEventsOf(userName);
        pDao.deleteAllPersons(associatedPeople);
        eDao.deleteAllEvents(associatedEvents);
    }




}
