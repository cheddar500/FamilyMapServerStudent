package fms.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fms.Exceptions.DataAccessException;
import fms.JsonSerializer;
import fms.Requests.LoginRequest;
import fms.Responses.LoginResponse;
import fms.Services.LoginService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Scanner;

/**
 * This class is used for queries with requests to serverIP:Port/user/login to create and return data from LoginService
 */
public class LoginHandler implements HttpHandler {

    /**
     * Default constructor
     */
    public LoginHandler(){}

    /**
     * Method for dealing with provided request and producing correct response
     * @param inputExchange an exchange which has the request provided by the client, use it produce response
     */
    @Override
    public void handle(HttpExchange inputExchange) throws IOException {
        System.out.println("Attempting to post "+inputExchange.getRequestURI());
        try{
            if(inputExchange.getRequestMethod().toUpperCase().equals("POST")) {
                Scanner scan = new Scanner(inputExchange.getRequestBody());
                StringBuilder sb = new StringBuilder();
                while (scan.hasNext()) {
                    sb.append(scan.next());
                    sb.append(" ");
                }
                String requestString = sb.toString();
                LoginRequest request = new JsonSerializer().deserialize(requestString, LoginRequest.class);
                LoginResponse response = new LoginService().login(request);
                if (!response.getSuccess()){
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    LoginResponse resp = new LoginResponse("Error: User/Password combo doesn't exist", false);
                    inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
                } else {
                    //send that it was ok
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    inputExchange.getResponseBody().write(response.getResponseBody().getBytes());
                }
            }
        } catch(DataAccessException | IOException e){
            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            LoginResponse resp = new LoginResponse(e.getMessage(), false);
            inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
            e.printStackTrace();
        }
        inputExchange.getResponseBody().close();
    }
}
