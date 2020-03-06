package fms.Dao;

import fms.Exceptions.DataAccessException;
import fms.Database;
import fms.Model.AuthToken;
import fms.Model.User;

import javax.xml.crypto.Data;
import java.sql.*;

/**
 * Object to get data from SQLite Users Table
 */
public class UserDao {


    private final Database db;

    /**
     * This is the constructor for UserDao
     * @param db Instances our connection
     */
    public UserDao(Database db) {
        this.db = db;
    }

    /**
     * Add a new user to the table
     * @param user User object to be added to the table
     */
    public void addUser(User user) throws DataAccessException {
        Connection conn = db.getConnection();
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
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while inserting user into the database");
        }
    }

    /**
     * Clear out all data from the User table in the database
     */
    public void clear() throws DataAccessException {
        Connection conn = db.getConnection();
        String sql = "DELETE FROM User";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while clearing User in the database");
        }
    }

    /**
     * If the specified User object exists, retrieves it from the database
     * @param userName User's username to get
     * @return User object with relevant data from table
     */
    public User getUser(String userName) throws DataAccessException {
        Connection conn = db.getConnection();
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
                db.closeConnection(true);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding user");
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
     * Log's specified User into the server
     * User is returned
     * @param userNameIn User's name in table
     * @param passwordIn User's associated password in table
     * @return User from table
     */
    public User login(String userNameIn, String passwordIn) throws DataAccessException {
        Connection conn = db.getConnection();
        User result = new User();
        //validate password, throw error if doesn't match
        String realPassword = null;
        String tempUserName = null;
        boolean validPassword = false;
        String sql = "SELECT * FROM User WHERE userName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userNameIn);
            ResultSet results = stmt.executeQuery();
            if(results.next())
            {
                tempUserName = results.getString("userName");
                realPassword = results.getString("password");
                result = new User(tempUserName, realPassword, results.getString("email"),
                        results.getString("firstName"), results.getString("lastName"),
                        results.getString("gender"), results.getString("personID"));
                if(realPassword.equals(passwordIn) && tempUserName.equals(userNameIn)){
                    validPassword = true;
                }
            }
            //throw an error if invalid password, include word "error" and 400 number
            if(!validPassword){
                db.closeConnection(false);
                throw new DataAccessException("Error 400 number : invalid password");
                //return null;
            }
            //generate a new unique authToken
//            AuthTokenDao atDao = new AuthTokenDao(conn);
//            String newAuthToken = atDao.generateAuthToken();
//            String newAuthToken = atDao.getAuthToken(userNameIn).getAuthToken();
            //add userName and authToken to AuthToken table
//            AuthToken at = new AuthToken(newAuthToken, userNameIn);
//            atDao.addAuthToken(at);
            db.closeConnection(true);
            //get user from User table and return them
            return result;

        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while logging user into the database");
        }
    }
}
