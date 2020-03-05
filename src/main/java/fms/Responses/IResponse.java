package fms.Responses;


/**
 * Helper class to get response body
 * Implemented by all Responses to override methods of use
 */
interface IResponse {
    /**
     * Helper method to get the response body
     * @return JSON data is returned to handler in a String
     */
    public String getResponseBody();

}
