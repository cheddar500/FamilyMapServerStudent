package fms.Dao;

import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.Person;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Object to get data from SQLite Person Table
 */
public class PersonDao {


    private final Database db;


    /**
     * This is the constructor for PersonDao
     * @param db Instances our connection
     */
    public PersonDao(Database db) {this.db = db;}


    /**
     * Add all Persons passed in to the table
     * @param personList List of Persons to be added
     */
    public void addAllPersons(List<Person> personList) throws DataAccessException {
        for (Person person : personList) {
            addPerson(person);
        }
    }

    /**
     * Add one Persons passed in to the table
     * @param person Person to be added
     */
    public void addPerson(Person person) throws DataAccessException {
        Connection conn = db.getConnection();
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while inserting person into the database");
        }
    }

    /**
     * Clear out all data from the Person table in the database
     */
    public void clear() throws DataAccessException {
        Connection conn = db.getConnection();
        String sql = "DELETE FROM Person";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while clearing person in the database");
        }
    }

    /**
     * Delete all Persons passed in to the table
     * @param personList List of Persons to be deleted
     */
    public void deleteAllPersons(List<Person> personList) throws DataAccessException {
        for (Person person : personList) {
            deletePerson(person.getPersonID());
        }
    }


    /**
     * Delete one Persons passed in to the table
     * @param personID  Person to be deleted
     */
    public void deletePerson(String personID ) throws DataAccessException {
        Connection conn = db.getConnection();
        String sql = "DELETE FROM Person WHERE personID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID );
            stmt.executeUpdate();
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while deleting person");
        }
    }


//    /**
//     * Delete one Persons passed in to the table
//     * @param associatedUsername  Person to be deleted
//     */
//    public void deletePerson(String associatedUsername ) throws DataAccessException {
//        String sql = "DELETE FROM Person WHERE associatedUsername = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, associatedUsername );
//            stmt.execute();
//        } catch (SQLException e) {
//            throw new DataAccessException("Error encountered while deleting person");
//        }
//    }

    /**
     * Get a Person from the database if they exist
     * @param personID Identifies the Person
     * @return Person from the database
     */
    public Person getPerson(String personID) throws DataAccessException {
        Connection conn = db.getConnection();
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE personID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                db.closeConnection(true);
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        db.closeConnection(false);
        return null;
    }

    /**
     * Get all Persons for a requested descendant User object
     * @param username Identifies User by username in the database
     * @return An ArrayList of Person objects
     */
    public ArrayList<Person> getPersonsOf(String username) throws DataAccessException {
        Connection conn = db.getConnection();
        ArrayList<Person> persons = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next()) {
                if(persons == null) persons = new ArrayList<>();
                persons.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID")));
            }
            db.closeConnection(true);
            return persons;
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding persons of person");
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
