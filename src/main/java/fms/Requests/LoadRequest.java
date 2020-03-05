package fms.Requests;

import fms.Model.Event;
import fms.Model.Person;
import fms.Model.User;

import java.util.ArrayList;

/**
 * LoadRequest class lets LoadHandler send info to the LoadService
 */
public class LoadRequest {

    /**
     * List of Events
     */
    private ArrayList<Event> events;
    /**
     * List of Persons
     */
    private ArrayList<Person> persons;
    /**
     * List of Users
     */
    private ArrayList<User> users;

    /**
     * Default constructor for LoadRequest
     */
    public LoadRequest(){
    }

    /**
     * Constructor to initialize all the variables for LoadRequest
     * @param events List of Events to be added to the database
     * @param persons List of Persons to be added to the database
     * @param users List of Users to be added to the database
     */
    public LoadRequest(ArrayList<Event> events, ArrayList<Person> persons, ArrayList<User> users) {
        this.events = events;
        this.persons = persons;
        this.users = users;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
