package fms.Dao;

import fms.Exceptions.DataAccessException;
import fms.Model.User;

import java.sql.*;

/**
 * Object to get data from SQLite Users Table
 */
public class UserDao {


    private final Connection conn;

    /**
     * This is the constructor for UserDao
     * @param conn Instances our connection
     */
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Add a new user to the table
     * @param user User object to be added to the table
     */
    public void addUser(User user) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO User (userName, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ud add user");
            throw new DataAccessException("Error encountered while inserting user into the database");
        }
    }

    /**
     * Clear out all data from the User table in the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ud clear");
            throw new DataAccessException("Error encountered while clearing User in the database");
        }
    }

    /**
     * If the specified User object exists, retrieves it from the database
     * @param userName User's username to get
     * @return User object with relevant data from table
     */
    public User getUser(String userName) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE userName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userName"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ud get user 1");
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("ud get user 2");
                }
            }

        }
        return null;
    }

}
