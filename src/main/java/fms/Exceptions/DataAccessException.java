package fms.Exceptions;

public class DataAccessException extends Exception {
    /**
     *
     * @param message pass on the error
     */
    public DataAccessException(String message)
    {
        super(message);
    }

    /**
     * Default constructor from super
     */
    DataAccessException()
    {
        super();
    }
}
