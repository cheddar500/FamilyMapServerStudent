package fms.Services;

import fms.Dao.AuthTokenDao;
import fms.Dao.EventDao;
import fms.Dao.PersonDao;
import fms.Dao.UserDao;
import fms.Database;
import fms.Exceptions.DataAccessException;
import fms.Responses.ClearResponse;

import java.sql.Connection;

/**
 * Clear interprets JSON Request and also completes the Clear request
 * by using the Dao package classes
 */
public class ClearService {
    /**
     * default constructor
     */
    public ClearService() {
    }

    /**
     * Clears all tables in the database
     * @return Returns ClearResult object with info about the request
     */
    public ClearResponse clear() throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        new AuthTokenDao(conn).clear();
        new EventDao(conn).clear();
        new PersonDao(conn).clear();
        new UserDao(conn).clear();
        String message = "Clear succeeded";
        db.closeConnection(true);
        return new ClearResponse(message, true);
    }
}
