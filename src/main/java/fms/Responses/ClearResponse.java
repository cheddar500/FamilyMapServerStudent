package fms.Responses;

import fms.JsonSerializer;

/**
 * Class with info about the Response of a Request to Clear
 */
public class ClearResponse implements IResponse {

    /**
     * Used to send user success/error messages
     */
    private String message;
    /**
     * Used to tell users if successful or not
     */
    private Boolean success;

    /**
     * default constructor
     */
    public ClearResponse(){
    }

    /**
     * constructor for ClearResponse
     * @param message Used to send user success/error messages
     * @param success Used to tell users if successful or not
     */
    public ClearResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }



    /**
     * Helper method to get the response body
     * @return JSON data is returned to handler in a String
     */
    @Override
    public String getResponseBody(){
        return new JsonSerializer().serialize(this, ClearResponse.class);
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
