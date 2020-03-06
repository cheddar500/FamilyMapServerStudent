package fms.Services;

import fms.Dao.AuthTokenDao;
import fms.Dao.PersonDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.Person;
import fms.Requests.PersonRequest;
import fms.Responses.PersonResponse;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Person interprets JSON Request and also completes the Person request
 * by using the Dao package classes
 */
public class PersonService {

    /**
     * default constructor
     */
    public PersonService() {
    }

    /**
     * Verifies the user and returns Person object(s)
     * @param request All the information about the request
     * @return PersonResponse object with info about Request
     * @throws DataAccessException
     */
    public PersonResponse getPerson(PersonRequest request) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        //use authToken get username, if null invalid
        AuthTokenDao atDao = new AuthTokenDao(db);
        String authToken = request.getAuthToken();
        String userName = atDao.getUserName(authToken);
        if(userName == null){
            String message = "Error: null username";
            return new PersonResponse(message, false);
        }

        //get all family members or single
        PersonDao pDao = new PersonDao(db);
        //if only requested to get one person
        if(!request.getGetAll()){
            Person result = pDao.getPerson(request.getPersonID());
            if(!result.getUsername().equals(userName)){
                String message = "Error: requested person doesn't belong to you";
                return new PersonResponse(message, false);
            }
            return new PersonResponse(userName,request.getPersonID(),result.getFirstName(),
            result.getLastName(), result.getGender(),result.getFatherID(),result.getMotherID(),
                    result.getSpouseID(),true);
        }
        //otherwise get all connected persons
        ArrayList<Person> personList;
        personList = pDao.getPersonsOf(userName);
        //two returns:
        // user has no persons --> null
        if (personList.size() == 0) {
            String message = "Error: user has no Person objects, null";
            return new PersonResponse(message, false);
        }
        //or successfully returned all people

        //if username from authToken down == single user, then belongs to them and is valid can give back info
        //else, doesn't belong to that user,
        //means try to match person obj from given personID to person obj from authToken, must match
        if(!request.getGetAll()) {
            try {
                String usernameFromID;
                String personID = request.getPersonID();
                Person tempPerson = pDao.getPerson(personID);
                usernameFromID = tempPerson.getUsername();
                if (!userName.equals(usernameFromID)) {
                    String message = "Error: authToken and personID don't match, invalid privileges";
                    return new PersonResponse(message, false);
                }
            } catch (Exception e) {
                String message = "Error: Failed to get all Person objects";
                return new PersonResponse(message, false);
            }
        }

        String message = "Successfully got all the Person objects";
        return new PersonResponse(message, true, personList);
    }
}
