package fms.Dao;

import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Model.AuthToken;

import java.sql.*;
import java.util.UUID;

/**
 * Object to get data from SQLite AuthToken Table
 */
public class AuthTokenDao {


    //private final Connection conn;
    private final Database db;


    /**
     * This is the constructor for AuthTokenDao
     * @param db Instances our connection
     */
    public AuthTokenDao(Database db) {this.db = db;}


    /**
     * Add a passed in AuthToken to table
     * @param authToken authToken to be added to table
     */
    public void addAuthToken(AuthToken authToken) throws DataAccessException {
        Connection conn = db.getConnection();
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO AuthToken (authToken, userName) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUserName());
            stmt.executeUpdate();
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Clear out all data from the AuthToken table in the database
     */
    public void clear() throws DataAccessException {
        Connection conn = db.getConnection();
        String sql = "DELETE FROM AuthToken";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while clearing AuthToken in the database");
        }
    }

    /**
     * Gets authToken for requested User
     * @param userName specifier for which User
     * @return Return authToken of specified User
     */
    public AuthToken getAuthToken(String userName) throws DataAccessException {
        Connection conn = db.getConnection();
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE userName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authToken"), rs.getString("userName"));
                db.closeConnection(true);
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding authToken");
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
     * Delete authToken of a requested User
     * @param authToken specifier for which authToken of the User since multiple sessions are possible
     */
    public void deleteAuthToken(String authToken) throws DataAccessException {
        Connection conn = db.getConnection();
        String sql = "DELETE FROM AuthToken WHERE authToken = "+authToken;
        try (Statement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate(sql);
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while deleting authToken");
        }
    }

    /**
     * Generate a new authToken
     * @return Return the new authToken
     */
    public String generateAuthToken(){
        return UUID.randomUUID().toString();
    }

    /**
     * Gets the username of User of provided authToken
     * @param authTokenIn specifier for which User
     * @return Returns username of provided authToken
     */
    public String getUserName(String authTokenIn) throws DataAccessException {
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM AuthToken";
        String result = null;
        try (Statement stmt = conn.createStatement()) {

            ResultSet results = stmt.executeQuery(sql);
            while (results.next())
            {
                String userName = results.getString("userName");
                String authToken = results.getString("authToken");
                if(authToken.equals(authTokenIn)){
                    result = userName;
                }
            }
            db.closeConnection(true);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while getting username from the database");
        }
        return result;
    }
}
