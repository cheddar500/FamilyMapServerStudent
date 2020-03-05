package fms.Requests;

/**
 * FillRequest class lets FillHandler send info to the FillService
 */
public class FillRequest {

    /**
     * Number of generations to be generated
     */
    private Integer numOfGenerations;
    /**
     * Requested userName for the User to fill with data
     */
    private String userName;

    /**
     * Empty constructor
     */
    public FillRequest(){}

    /**
     * Constructor to initialize all the variables for FillRequest
     * @param numOfGenerations Number of generations to be generated
     * @param userName Requested userName for the User to fill with data
     */
    public FillRequest(Integer numOfGenerations, String userName) {
        this.numOfGenerations = numOfGenerations;
        this.userName = userName;
    }

    public Integer getNumOfGenerations() {
        return numOfGenerations;
    }

    public String getUserName() {
        return userName;
    }

    public void setNumOfGenerations(Integer numOfGenerations) {
        this.numOfGenerations = numOfGenerations;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
