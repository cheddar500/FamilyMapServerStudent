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
        AuthTokenDao atDao = new AuthTokenDao(conn); //use authToken get username, if null invalid
        String authToken = request.getAuthToken();
        String userName = atDao.getUserName(authToken);
        if(userName == null){
            db.closeConnection(true);
            String message = "Error: username is null";
            return new EventResponse(message, false);
        }
        EventDao eDao = new EventDao(conn); //get all family members or single
        String userOwner = eDao.getEvent(request.getEventID()).getUsername();
        if(!userOwner.equals(userName)){ //make sure event(s) belong to the requestee
            db.closeConnection(true);
            return new EventResponse("Error: Event does not belong to you", false);
        }
        if(!request.getGetAll()){ //if only requested to get one event
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
        ArrayList<Event> eventList = eDao.getEventsOf(userName); //otherwise get all connected events
        if (eventList.size() == 0) {
            db.closeConnection(true);
            String message = "Error: user has no Event objects, null";
            return new EventResponse(message, false);
        }
        db.closeConnection(true);
        return new EventResponse(eventList, true);
    }
}
