package fms.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fms.Exceptions.DataAccessException;
import fms.JsonSerializer;
import fms.Requests.RegisterRequest;
import fms.Responses.RegisterResponse;
import fms.Services.RegisterService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Scanner;

/**
 * This class is used for queries with requests to serverIP:Port/user/register to create and return data from RegisterService
 */
public class RegisterHandler implements HttpHandler {

    /**
     * Default constructor
     */
    public RegisterHandler(){}

    /**
     * Method for dealing with provided request and producing correct response
     * @param inputExchange an exchange which has the request provided by the client, use it produce response
     */
    @Override
    public void handle(HttpExchange inputExchange) throws IOException {
        System.out.println("Attempting to post "+inputExchange.getRequestURI());
        try{
            Scanner scan = new Scanner(inputExchange.getRequestBody());
            StringBuilder sb = new StringBuilder();
            while(scan.hasNext()){
                sb.append(scan.next()); sb.append(" ");
            }
            String requestString = sb.toString();
            RegisterRequest request = JsonSerializer.deserialize(requestString, RegisterRequest.class);
            RegisterResponse response = new RegisterService().register(request);
            if(!response.getSuccess()){
                inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                RegisterResponse resp = new RegisterResponse("Error: user already exists", false);
                inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
            } else {
                //send that it was ok
                inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                //give me where I need to write what happened -> write the response we got
                inputExchange.getResponseBody().write(response.getResponseBody().getBytes());
            }
        }catch(DataAccessException e){
            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            RegisterResponse resp = new RegisterResponse(e.getMessage(), false);
            inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
            e.printStackTrace();
        }
        inputExchange.close();
    }
}
