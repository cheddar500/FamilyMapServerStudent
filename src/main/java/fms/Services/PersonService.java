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
        Database db = new Database(); Connection conn = db.openConnection();
        AuthTokenDao atDao = new AuthTokenDao(conn); //use authToken get username, if null invalid
        String authToken = request.getAuthToken();
        String userName = atDao.getUserName(authToken);
        if(userName == null){
            db.closeConnection(true);
            String message = "Error: null username";
            return new PersonResponse(message, false);
        }
        PersonDao pDao = new PersonDao(conn); //get all family members or single
        String userOwner = pDao.getPerson(request.getPersonID()).getUsername();
        if(!userOwner.equals(userName)){ //make sure event(s) belong to the requestee
            db.closeConnection(true);
            String message2 = "Error: Person does not belong to you";
            return new PersonResponse(message2, false);
        }
        if(!request.getGetAll()){ //if only requested to get one person
            Person result = pDao.getPerson(request.getPersonID());
            if(result == null){
                db.closeConnection(true);
                return new PersonResponse("Error: Person does not exist", false);
            }
            db.closeConnection(true);
            return new PersonResponse(userName,request.getPersonID(),result.getFirstName(),
            result.getLastName(), result.getGender(),result.getFatherID(),result.getMotherID(),
                    result.getSpouseID(),true);
        }
        ArrayList<Person> personList  = pDao.getPersonsOf(userName); //otherwise get all connected persons
        if (personList.size() == 0) {
            db.closeConnection(true);
            return new PersonResponse("Error: user has no Person objects, null", false);
        }
        db.closeConnection(true);
        return new PersonResponse("Successfully got all the Person objects", true, personList);
    }
}
