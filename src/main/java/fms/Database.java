package fms;

import fms.Exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the connection to the database
 */
public class Database {


    static {
        try{
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * SQL connection to the database
     */
    private Connection conn;

    //**********************************************************************************************

    /**
     * default constructor
     */
    public Database(){
    }

    //**********************************************************************************************

    public Connection getConnection() throws DataAccessException {
        System.out.println("Getting DB Connection");
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    //**********************************************************************************************

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    //**********************************************************************************************

    /**
     * closes the connection with the database
     * @param commit Tells to keep or discard any changes
     */
    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER
    public void closeConnection(boolean commit) throws DataAccessException {
        System.out.println("Closing DB Connection");
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    //**********************************************************************************************

    /**
     * clears all of the tables
     */
    public void clearTables() throws DataAccessException
    {

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Event";
            stmt.executeUpdate(sql);
            String sq2 = "DELETE FROM Person";
            stmt.executeUpdate(sq2);
            String sq3 = "DELETE FROM User";
            stmt.executeUpdate(sq3);
            String sq4 = "DELETE FROM AuthToken";
            stmt.executeUpdate(sq4);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    //**********************************************************************************************

    /**
     * Create any needed tables that do not exist
     */
    public void createTables(){

    }


    //**********************************************************************************************
}
