package fms.Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fms.Exceptions.DataAccessException;
import fms.Responses.ClearResponse;
import fms.Services.ClearService;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * This class is used for queries with requests to serverIP:Port/clear to create and return data from ClearService
 */
public class ClearHandler implements HttpHandler {

    /**
     * Default constructor
     */
    public ClearHandler(){}

    /**
     * Method for dealing with provided request and producing correct response
     * @param inputExchange an exchange which has the request provided by the client, use it produce response
     */
    @Override
    public void handle(HttpExchange inputExchange) throws IOException {
        //Post: send data to server for processing
        System.out.println("Attempting to post " + inputExchange.getRequestURI());
        try{
            if (inputExchange.getRequestMethod().toUpperCase().equals("POST")) {
                //
                ClearResponse clearMe = new ClearService().clear();
                if(!clearMe.getSuccess()){
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    ClearResponse resp = new ClearResponse("Error: user already exists", false);
                    inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
                } else {
                    //send that it was ok
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    //HttpExchange getResponseBody then ClearResponse getResponseBody
                    //send the data
                    //give me where I need to write what happened -> write the response we got
                    inputExchange.getResponseBody().write(clearMe.getResponseBody().getBytes());
                }
            }

        } catch (DataAccessException e) {
            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            ClearResponse resp = new ClearResponse(e.getMessage(), false);
            inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
            e.printStackTrace();
        }
        inputExchange.close();
    }

}
