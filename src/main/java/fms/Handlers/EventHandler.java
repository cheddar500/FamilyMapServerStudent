package fms.Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fms.Exceptions.DataAccessException;
import fms.Requests.EventRequest;
import fms.Responses.EventResponse;
import fms.Services.EventService;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * This class is used for queries with requests to serverIP:Port/event to create and return data from EventService
 */
public class EventHandler implements HttpHandler {

    /**
     * Default constructor
     */
    public EventHandler(){}

    /**
     * Used to decide whether to return a single or multiple events
     * @param urlPathName The Path that the exchange gave
     * @return Returns a string with one eventID or null
     */
    private String getOneEvent(String urlPathName){
        final int EVENT_ID_LENGTH = 7;
        if(urlPathName.length() > EVENT_ID_LENGTH){
            return urlPathName.substring(EVENT_ID_LENGTH);
        }
        return null;
    }

    /**
     * Method for dealing with provided request and producing correct response
     * @param inputExchange an exchange which has the request provided by the client, use it produce response
     */
    @Override
    public void handle(HttpExchange inputExchange) throws IOException{
        System.out.println("Attempting to get "+inputExchange.getRequestURI());
        try{
            if(inputExchange.getRequestMethod().toUpperCase().equals("GET")){
                // Get the HTTP request headers
                Headers reqHeaders = inputExchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    //verify token?
                    String personID = getOneEvent(inputExchange.getRequestURI().toString());
                    EventRequest request = new EventRequest(authToken, personID, (personID == null));
                    EventResponse response = new EventService().getEvent(request);
                    if(!response.getSuccess()){//clear test response is null here
                        inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        EventResponse resp = new EventResponse("Error: user already exists", false);
                        inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
                    } else {
                        //send that it was ok
                        inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        //HttpExchange getResponseBody then ClearResponse getResponseBody
                        //send the data
                        //give me where I need to write what happened -> write the response we got
                        inputExchange.getResponseBody().write(response.getResponseBody().getBytes());
                    }
                } else {
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    EventResponse resp = new EventResponse("Error: invalid authToken", false);
                    inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
                }
            }
        } catch(DataAccessException | IOException e){
            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
//            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            EventResponse resp = new EventResponse(e.getMessage(), false);
            inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
            e.printStackTrace();
        }
        inputExchange.close();
    }

}
