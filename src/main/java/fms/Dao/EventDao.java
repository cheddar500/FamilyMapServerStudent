package fms.Dao;

import fms.Exceptions.DataAccessException;
import fms.Model.Event;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Object to get data from SQLite Event Table
 */
public class EventDao {


    private final Connection conn;

    /**
     * This is the constructor for EventDao
     * @param conn Instances our connection
     */
    public EventDao(Connection conn) {this.conn = conn;}


    /**
     * Add all the Event passed in
     * @param eventList list of Event to be added
     */
    public void addAllEvents(List<Event> eventList) throws DataAccessException {
        for (int i = 0; i < eventList.size(); i++) {
            addEvent(eventList.get(i));
        }
    }

    /**
     * Add an Event passed in
     * @param event Event to be added
     */
    public void addEvent(Event event) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Event (eventID, associatedUsername, personID, latitude, longitude," +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting event into the database");
        }
    }

    /**
     * Clear out all data from the Event table in the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Event";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing event in the database");
        }
    }

    /**
     * Delete all the Event passed in
     * @param eventList list of Event to be deleted
     */
    public void deleteAllEvents(List<Event> eventList) throws DataAccessException {
        for (int i = 0; i < eventList.size(); i++) {
            deleteEvent(eventList.get(i).getEventID());
        }
    }


    /**
     * Delete an Event passed in
     * @param eventID Event to be deleted
     */
    public void deleteEvent(String eventID) throws DataAccessException {
        String sql = "DELETE FROM Event WHERE eventID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID );
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting event");
        }
    }

    /**
     * Get a requested Event from the Event table
     * @param eventID Specifier for which Event
     * @return Return requested Event from Event table
     */
    public Event getEvent(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE eventID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Gets all Event for requested User
     * @param username Specifier for which User
     * @return Return all Event for requested USer from table
     */
    public ArrayList<Event> getEventsOf(String username) throws DataAccessException {
        ArrayList<Event> events = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next()) {
                if(events == null) events = new ArrayList<>();
                events.add(new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year")));
            }
            conn.commit();
            stmt.close();
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding events of event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
