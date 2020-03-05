package fms.Services;


import fms.Dao.AuthTokenDao;
import fms.Dao.EventDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.Event;
import fms.Requests.EventRequest;
import fms.Responses.EventResponse;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Event interprets JSON Request and also completes the Event request
 * by using the Dao package classes
 */
public class EventService {
    /**
     * default constructor
     */
    public EventService() {
    }


//    /**
//     * Gets an Event from the database
//     * @param eventID Unique identifier for this event (non-empty string)
//     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
//     * @return Returns Event object with info about the request
//     */
//    EventService getEvent(String eventID, String eventType){
//        return null;
//    }

    /**
     * Verifies the event and returns Event object(s)
     * @param request All the information about the request
     * @return EventResponse object with info about Request
     * @throws DataAccessException
     */
    public EventResponse getEvent(EventRequest request) throws DataAccessException{
        Database db = new Database();
        Connection conn = db.openConnection();
        //use authToken get username, if null invalid
        AuthTokenDao atDao = new AuthTokenDao(conn);
        String authToken = request.getAuthToken();
        String userName = atDao.getUserName(authToken);
        if(userName == null){
            return null;
        }

        //get all family members or single
        EventDao eDao = new EventDao(conn);
        //if only requested to get one person
        if(!request.getGetAll()){
            Event result = new Event();
            String message = "Successfully got one Event";
            result = eDao.getEvent(request.getEventID());
            return new EventResponse(
                    result.getCity(),
                    result.getCountry(),
                    userName,
                    result.getEventID(),
                    result.getEventType(),
                    (float) result.getLatitude(),
                    (float) result.getLongitude(),
                    result.getPersonID(),
                    result.getYear(),
                    message,
                    true);
        }
        //otherwise get all connected persons
        ArrayList<Event> eventList = new ArrayList<>();
        eventList = eDao.getEventsOf(userName);
        //two returns:
        // user has no persons --> null
        if (eventList.size() == 0) {
            db.closeConnection(true);
            String message = "Error: user has no Event objects, null";
            return new EventResponse(message, false);
        }
        //or successfully returned all people

        //if username from authToken down == single user, then belongs to them and is valid can give back info
        //else, doesn't belong to that user,
        //means try to match person obj from given eventID to person obj from authToken, must match
        if(!request.getGetAll()) {
            try {
                String usernameFromID = null;
                String eventID = request.getEventID();
                Event tempPerson = eDao.getEvent(eventID);
                usernameFromID = tempPerson.getUsername();
                if (!userName.equals(usernameFromID)) {
                    db.closeConnection(true);
                    String message = "Error: authToken and eventID don't match, invalid privileges";
                    return new EventResponse(message, false);
                }
            } catch (Exception e) {
                db.closeConnection(true);
                String message = "Error: Failed to get all Event objects";
                return new EventResponse(message, false);
            }
        }

        db.closeConnection(true);
        String message = "Successfully got all the Event objects";
        return new EventResponse(eventList, message, true);
    }
}
