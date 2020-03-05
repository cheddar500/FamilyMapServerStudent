package fms.Responses;

import fms.JsonSerializer;

/**
 * Class with info about the Response of a FillRequest
 */
public class FillResponse implements IResponse {

    /**
     * Tells seccess/error messages
     */
    private String message;
    /**
     * Tells if completed successfully or not
     */
    private Boolean success;

    /**
     * default constructor
     */
    public FillResponse() {
    }

    /**
     * constructor for FillResponse
     * @param message Tells success/error messages
     * @param success Tells if completed successfully or not
     */
    public FillResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }


    /**
     * Helper method to get the response body
     * @return JSON data is returned to handler in a String
     */
    @Override
    public String getResponseBody(){
        return JsonSerializer.serialize(this, FillResponse.class);
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
