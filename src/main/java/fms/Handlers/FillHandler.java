package fms.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fms.Exceptions.DataAccessException;
import fms.Requests.FillRequest;
import fms.Responses.FillResponse;
import fms.Services.FillService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * This class is used for queries with requests to serverIP:Port/fill/[username]/{generations} to create and return data from FillService
 */
public class FillHandler implements HttpHandler {

    /**
     * Default constructor
     */
    public FillHandler(){}


    /**
     * Parses the number of generations requested for generation
     * @param urlPathName The Path that the exchange gave
     * @return Returns an Integer equal to requested number of generations or null when the / doesn't exist
     */
    private Integer getGenerations(String urlPathName){
        final int USERNAME_LENGTH = 6; // /fill/ then username
        if(urlPathName.length() > USERNAME_LENGTH){
            char[] nameNum = urlPathName.substring(USERNAME_LENGTH).toCharArray();
            String nameNumString = urlPathName.substring(USERNAME_LENGTH);
            int slashCount = 0;
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < nameNum.length; i++) {
                if(slashCount == 1){
                    result.append(nameNum[i]);
                }
                if(nameNum[i] == '/'){
                    slashCount++;
                }
            }
            if(slashCount < 1){
                return 4;
            }
            else {
                Integer finalAnswer = Integer.parseInt(result.toString());
                if (finalAnswer > -1) {
                    return finalAnswer;
                } else {
                    return null;
                }
            }
        }
        return null;

    }

    /**
     * Parses the userName from the URL of the User
     * @param urlPathName The Path that the exchange gave
     * @return Returns a String that is the userName
     */
    private String getUserName(String urlPathName){
        final int USERNAME_LENGTH = 6; // /fill/ then username
        if(urlPathName.length() > USERNAME_LENGTH){
            char[] nameNum = urlPathName.substring(USERNAME_LENGTH).toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nameNum.length; i++) {
                //number of generations specified, get just the username
                if(nameNum[i] == '/'){
                    return sb.toString();
                }
                sb.append(nameNum[i]);
            }
            //there was no number of generations specified
            return sb.toString();
        }
        return null;
    }

    /**
     * Method for dealing with provided request and producing correct response
     * @param inputExchange an exchange which has the request provided by the client, use it produce response
     */
    @Override
    public void handle(HttpExchange inputExchange) throws IOException {
        System.out.println("Attempting to post "+inputExchange.getRequestURI());
        String userName = getUserName(inputExchange.getRequestURI().toString());
        Integer generations = getGenerations(inputExchange.getRequestURI().toString());
        try{
            if(inputExchange.getRequestMethod().toUpperCase().equals("POST")){
                FillRequest request = new FillRequest(generations, userName);
                FillResponse response = new FillService().fill(request);
                //send that it was ok
                inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                //give me where I need to write what happened -> write the response we got
                inputExchange.getResponseBody().write(response.getResponseBody().getBytes());

            }
        } catch(DataAccessException | SQLException e){
            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            FillResponse resp = new FillResponse(e.getMessage(), false);
            inputExchange.getResponseBody().write(resp.getResponseBody().getBytes());
            e.printStackTrace();
        }
        inputExchange.close();
    }
}
