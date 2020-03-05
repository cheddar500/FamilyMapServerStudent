package fms.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Handles queries that contain requests not handled by other classes
 */
public class FileHandler implements HttpHandler {

    /**
     * Default constructor
     */
    public FileHandler() {
    }

    /**
     * Method for dealing with provided request and producing correct response
     * @param inputExchange an exchange which has the request provided by the client, use it produce response
     * @throws NullPointerException if inputExchange is <code>null</code>
     */
    @Override
    public void handle(HttpExchange inputExchange) throws IOException {
        System.out.println("Attempting to find " + inputExchange.getRequestURI());
        try{
            //ignore everything that isn't a GET
            //could send a 405 (Method not allowed)
            if(inputExchange.getRequestMethod().toUpperCase().equals("GET")){
                Headers reqHeaders = inputExchange.getRequestHeaders();
                OutputStream outStream = inputExchange.getResponseBody();
                FileInputStream fileInStream;
                String uriInfo = inputExchange.getRequestURI().toString();

                if(uriInfo.equals("/") || uriInfo == null){
                    fileInStream = new FileInputStream(new File("web"+File.separator+"index.html"));
                    inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    System.out.println("Returned: web/index.html");
                } else {
                    if((uriInfo.length() >= 5) && uriInfo.substring(0,5).contains("/web/")){
                        uriInfo = uriInfo.substring(4);
                    }
                    File returnFile = new File("web"+uriInfo);
                    if(returnFile.isFile()){
                        fileInStream = new FileInputStream(returnFile);
                        inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    } else {
                        uriInfo = File.separator + "HTML" + File.separator + "404.html";
                        fileInStream = new FileInputStream(new File("web"+uriInfo));
                        inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND,0);
                    }
                    System.out.println("Returned: web"+uriInfo);
                }
                outStream.write(fileInStream.readAllBytes());
                outStream.close();
                inputExchange.close();
            }
        } catch(IOException e){
            inputExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            inputExchange.getResponseBody().close();
            e.printStackTrace();
        }


    }
}
