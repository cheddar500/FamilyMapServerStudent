package fms;

import com.sun.net.httpserver.HttpServer;
import fms.Handlers.*;

import java.net.InetSocketAddress;
import java.io.IOException;

/**
 * This is the leading server for the Family Map that will
 * take care of all requests
 * The most requests it can take at once, backlog max, is a const 5
 */
public class Server {

    //http://127.0.0.1:8080/

    /**
     * Default constructor
     */
//    @SuppressWarnings("unused")
    public Server() {
    }

    /**
     * Amount of requests that will build up before being denied by the server
     */
    private static final int BACKLOG_MAX = 5;

    /**
     * The staring place for our Family Map Server
     * @param args parameters passed in via command line
     */
    public static void main(String[] args){
        try {
            startServer(Integer.parseInt(args[0]));
        } catch (IOException e) {
            System.out.println("Server unable to start. Tried port "+args[0]);
        }
    }

    /**
     * Makes server understand context of input
     * @param serverObj This server object wil contain the listed handlers
     */
    private static void registerHandlers(HttpServer serverObj) {
        //create context
        //Formatting: serverObj.createContext("url", handlerObj);
        serverObj.createContext("/", new FileHandler());
        serverObj.createContext("/user/register", new RegisterHandler());
        serverObj.createContext("/user/login", new LoginHandler());
        serverObj.createContext("/clear", new ClearHandler());
        serverObj.createContext("/fill", new FillHandler());
        serverObj.createContext("/load", new LoadHandler());
        serverObj.createContext("/person", new PersonHandler());
        serverObj.createContext("/event", new EventHandler());
    }

    /**
     * Start the server
     * @param requestedPort requested port, only valid if 1-65535
     * @throws IOException thrown if not handled here
     */
    private static void startServer(int requestedPort) throws IOException{
        InetSocketAddress serverAddress = new InetSocketAddress(requestedPort);
        HttpServer serverObj = HttpServer.create(serverAddress, BACKLOG_MAX);
        registerHandlers(serverObj);
        serverObj.start();
        System.out.println("Server for Family Map has started and is using port "+requestedPort);
    }

}
