package fms.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fms.Exceptions.DataAccessException;
import fms.JsonSerializer;
import fms.Requests.LoadRequest;
import fms.Responses.LoadResponse;
import fms.Services.LoadService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Scanner;

/**
 * This class is used for queries with requests to serverIP:Port/load to create and return data from LoadService
 */
public class LoadHandler implements HttpHandler {

    /**
     * Default constructor
     */
    public LoadHandler(){}

    /**
     * Method for dealing with provided request and producing correct response
     * @param inputExchange an exchange which has the request provided by the client, use it produce response
     */
    @Override
    public void handle(HttpExchange inputExchange) throws IOException {
        System.out.println("Attempting to post "+inputExchange.getRequestURI());
        try{
            if(inputExchange.getRequestMethod().toUpperCase().equals("POST")){
                Scanner scan = new Scanner(inputExchange.getRequestBody());
                StringBuilder sb = new StringBuilder();
                while(scan.hasNext()){
                    sb.append(scan.next()); sb.append(" ");
                }
                String requestString = sb.toString();
                LoadRequest request = new JsonSerializer().deserialize(requestString, LoadRequest.class);
                LoadResponse response = new LoadService().load(request);
                if(!response.getSuccess()){
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    LoadResponse resp = new LoadResponse("Error: user already exists", false);
                    inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
                } else {
                    //send that it was ok
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    //give me where I need to write what happened -> write the response we got
                    inputExchange.getResponseBody().write(response.getResponseBody().getBytes());
                }
            }
        } catch(DataAccessException e){
            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            LoadResponse resp = new LoadResponse(e.getMessage(), false);
            inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
            e.printStackTrace();
        }
        inputExchange.getResponseBody().close();
    }
}
