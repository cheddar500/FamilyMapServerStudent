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
        new AuthTokenDao(db).clear();
        new EventDao(db).clear();
        new PersonDao(db).clear();
        new UserDao(db).clear();
        String message = "Clear succeeded";
        return new ClearResponse(message, true);
    }
}
