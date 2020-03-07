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
            db.closeConnection(true);
            String message = "Error: username is null";
            return new EventResponse(message, false);
        }

        //get all family members or single
        EventDao eDao = new EventDao(conn);
        String userOwner = eDao.getEvent(request.getEventID()).getUsername();
        //make sure event(s) belong to the requestee
        if(!userOwner.equals(userName)){
            db.closeConnection(true);
            String message2 = "Error: Event does not belong to you";
            return new EventResponse(message2, false);
        }
        //if only requested to get one person
        if(!request.getGetAll()){
            Event result = eDao.getEvent(request.getEventID());
            if(result == null){
                db.closeConnection(true);
                String message2 = "Error: Event does not exist";
                return new EventResponse(message2, false);
            }
            db.closeConnection(true);
            return new EventResponse(
                    userName, result.getEventID(), result.getPersonID(), result.getLatitude(),
                    result.getLongitude(), result.getCountry(),
                    result.getCity(), result.getEventType(), result.getYear(), true);
        }
        //otherwise get all connected persons
        ArrayList<Event> eventList = new ArrayList<>();
        eventList = eDao.getEventsOf(userName);
        //two returns:
        // user has no events --> null
        if (eventList.size() == 0) {
            db.closeConnection(true);
            String message = "Error: user has no Event objects, null";
            return new EventResponse(message, false);
        }
        //or successfully returned all people

        //if username from authToken down == single user, then belongs to them and is valid can give back info
        //else, doesn't belong to that user,
        //means try to match person obj from given eventID to person obj from authToken, must match
//        if(!request.getGetAll()) {
//                try {
//                    String usernameFromID = null;
//                    String eventID = request.getEventID();
//                    Event tempPerson = eDao.getEvent(eventID);
//                    usernameFromID = tempPerson.getUsername();
//                    if (!userName.equals(usernameFromID)) {
//                        db.closeConnection(true);
//                        String message = "Error: authToken and eventID don't match, invalid privileges";
//                        return new EventResponse(message, false);
//                    }
//                } catch (Exception e) {
//                    db.closeConnection(true);
//                    String message = "Error: Failed to get all Event objects";
//                    return new EventResponse(message, false);
//                }
//        }

        db.closeConnection(true);
        return new EventResponse(eventList, true);
    }
}
