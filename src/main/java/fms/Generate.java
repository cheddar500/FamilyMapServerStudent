package fms;


import fms.Dao.EventDao;
import fms.Dao.PersonDao;
import fms.Exceptions.DataAccessException;
import fms.Model.Event;
import fms.Model.Person;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Generate {

    private EventDao eDao;
    private int year;
    private int generationCounter;
    private ArrayList<Event> events;
    private ArrayList<Person> persons;
    private final int CURRENT_YEAR = 2025;


    /**
     * Default constructor
     */
    public Generate(){}


    Database db;
    Connection conn;

    /**
     * Used to start the process for generating user ancestor data
     * @param userPerson the person to generate data for
     * @param generation the number of generation to generate data about
     * @throws DataAccessException oh no, hope this doesnt happen
     * @throws IOException also hope it doesnt happen
     */
    public void generateInfo(Person userPerson, int generation) throws DataAccessException, IOException {
        db = new Database();
        conn = db.openConnection();
        PersonDao pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        //Add at least the original user
        events = new ArrayList<>();
        persons = new ArrayList<>();
        persons.add(userPerson);
        year = CURRENT_YEAR - 25;
        generationCounter = 0;
        //Original user has to have at least a birth
        events.add(generateEvent(userPerson.getUsername(),userPerson.getPersonID(),getRandomLocation(),
                "birth",year));
        //Add additional generations as requested recursively
        recurse(userPerson, generation, events, persons, eDao);
        pDao.addAllPersons(persons);
        eDao.addAllEvents(events);
        db.closeConnection(true);
    }

    /**
     *  Recursively generate ancestor data for the user
     * @param child the child we are generating parents for
     * @param generation how many more generations to generate
     * @param events a list to store new events to be added to the database in
     * @param persons a list to store new persons to be added to the database in
     * @param eDao use to access events to generate correct years
     * @throws DataAccessException
     * @throws IOException
     */
    private void recurse(Person child, int generation, ArrayList<Event> events, ArrayList<Person> persons, EventDao eDao) throws DataAccessException, IOException {
        //base case
        if(generation == 0) { return; }
        //Father Data
        String fathersFatherID = getParentID(generation-1);
        String fathersMotherID = getParentID(generation-1);
        String mFirstName = getMaleName();
        String fatherPersonID = child.getFatherID();
        //Mother Data
        String mothersFatherID = getParentID(generation-1);
        String mothersMotherID = getParentID(generation-1);
        String fFirstName = getFemaleName();
        String motherPersonID = child.getMotherID();
        //Gender neutral data
        String lastName = getSurname();
        String associatedUsername = child.getUsername();
        db.openConnection();
        int year = eDao.getEvent(child.getPersonID()+"-"+"birth").getYear();
        db.closeConnection(true);
        generationCounter++;
        Location marriageLocation = getRandomLocation();
        //Generate and link father of person
        Person father = new Person(fatherPersonID, associatedUsername, mFirstName, lastName, "m",
                fathersFatherID, fathersMotherID, motherPersonID);
        persons.add(father);
        //Generate birth, marriage, death
        events.add(generateEvent(child.getUsername(), father.getPersonID(), getRandomLocation(),
                "birth",year-25));
        events.add(generateEvent(child.getUsername(), father.getPersonID(), marriageLocation,
                "marriage",year-5));
        events.add(generateEvent(child.getUsername(), father.getPersonID(), getRandomLocation(),
                "death",year+30));
        recurse(father, generation-1, events, persons, eDao);
        //Generate and link mother of person
        Person mother = new Person(motherPersonID, associatedUsername, fFirstName, lastName, "f",
                mothersFatherID, mothersMotherID, fatherPersonID);
        persons.add(mother);
        //Generate birth, marriage, death
        events.add(generateEvent(child.getUsername(), mother.getPersonID(), getRandomLocation(),
                "birth",year-25));
        events.add(generateEvent(child.getUsername(), mother.getPersonID(), marriageLocation,
                "marriage",year-5));
        events.add(generateEvent(child.getUsername(), mother.getPersonID(), getRandomLocation(),
                "death",year+30));
        recurse(mother, generation-1, events, persons, eDao);
    }


    /**
     * Generate an event
     * @param associatedUsername user the event is attached to
     * @param personID persons ID
     * @param location where it happened
     * @param eventType what happened
     * @param year when it happened
     * @return an event
     * @throws IOException
     */
    private Event generateEvent(String associatedUsername, String personID, Location location, String eventType, int year) throws IOException {
        JsonStrings jStrings = new JsonStrings();
        String locations = jStrings.getLocations();
        LocationData jsonLocations = new JsonSerializer().deserialize(locations, LocationData.class);
        ArrayList<Location> locationList = jsonLocations.getData();
        String eventID = personID+"-"+eventType;
        return new Event(eventID, associatedUsername, personID,location.getLatitude(),
                location.getLongitude(),location.getCountry(),location.getCity(),eventType,year);
    }

    /**
     * get the parents ID
     * @param generations how many more generations are left to generate
     * @return
     */
    private String getParentID(int generations){
        if(generations == 0){
            return null;
        }
        else{ //get random
            return UUID.randomUUID().toString();
        }
    }

    /**
     * Get a random male name
     * @return a random male name String
     * @throws IOException
     */
    private String getMaleName() throws IOException {
        JsonStrings jStrings = new JsonStrings();
        String mnames = jStrings.getMnames();
        NameData maleNames = new JsonSerializer().deserialize(mnames, NameData.class);
        Random rand = new Random();
        ArrayList<String> finalNames = maleNames.getData();
        return finalNames.get(rand.nextInt(finalNames.size()));
    }

    /**
     * Get a random female name
     * @return a random female name String
     * @throws IOException
     */
    private String getFemaleName() throws IOException {
        JsonStrings jStrings = new JsonStrings();
        String fnames = jStrings.getFnames();
        NameData femaleNames = new JsonSerializer().deserialize(fnames, NameData.class);
        Random rand = new Random();
        ArrayList<String> finalNames = femaleNames.getData();
        return finalNames.get(rand.nextInt(finalNames.size()));
    }

    /**
     * Get a random surname
     * @return a random surname String
     * @throws IOException
     */
    private String getSurname() throws IOException {
        JsonStrings jStrings = new JsonStrings();
        String snames = jStrings.getSnames();
        NameData surnames = new JsonSerializer().deserialize(snames, NameData.class);
        Random rand = new Random();
        ArrayList<String> finalNames = surnames.getData();
        return finalNames.get(rand.nextInt(finalNames.size()));
    }

    /**
     * Get a random location
     * @return a location object
     * @throws IOException
     */
    private Location getRandomLocation() throws IOException {
        JsonStrings jStrings = new JsonStrings();
        String jsonLocations = jStrings.getLocations();
        LocationData ld = new JsonSerializer().deserialize(jsonLocations, LocationData.class);
        ArrayList<Location> locations = ld.getData();
        Random rand = new Random();
        return locations.get(rand.nextInt(locations.size()));
    }



    //******************************************************************************************************************

    /**
     * Definition of location, used to generate Event objects in outer class
     */
    private class Location{
        /**
         * The country of the location
         */
        private String country;
        /**
         * The city of the location
         */
        private String city;
        /**
         * The longitude of the location
         */
        private Float longitude;
        /**
         * The latitude of the location
         */
        private Float latitude;

        /**
         * Default constructor
         */
        public Location(){}

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

        public Float getLongitude() {
            return longitude;
        }

        public Float getLatitude() {
            return latitude;
        }
    }

    //******************************************************************************************************************

    /**
     * The middle man between the given JSON and giving the location info to the outer class
     */
    private class LocationData{
        /**
         * The locations from the JSON
         */
        private ArrayList<Location> data;

        /**
         * Default constructor
         */
        LocationData(){}

        /**
         * get the JSON data
         * @return the JSON data about locations
         */
        ArrayList<Location> getData() { return data; }
    }

    //******************************************************************************************************************

    /**
     * The middle man between the given JSON and giving it as usable name data to the outer class
     */
    private class NameData{
        /**
         * The names from the JSON
         */
        private ArrayList<String> data;

        /**
         * Default constructor
         */
        NameData(){}

        /**
         * get the JSON data
         * @return the JSON data about locations
         */
        ArrayList<String> getData() { return data; }
    }

    //******************************************************************************************************************

    private class JsonStrings{
        /**
         * provided JSON female names
         */
        String fnames;
        /**
         * provided JSON male names
         */
        String mnames;
        /**
         * provided JSON surnames
         */
        String snames;
        /**
         * provided JSON locations
         */
        String locations;

        /**
         * Default constructor
         */
        JsonStrings() throws IOException {
            fnames = new String(Files.readAllBytes(Paths.get("json/fnames.json")));
            mnames = new String(Files.readAllBytes(Paths.get("json/mnames.json")));
            snames = new String(Files.readAllBytes(Paths.get("json/snames.json")));
            locations = new String(Files.readAllBytes(Paths.get("json/locations.json")));
        }

        public String getFnames() {
            return fnames;
        }

        public String getMnames() {
            return mnames;
        }

        public String getSnames() {
            return snames;
        }

        public String getLocations() {
            return locations;
        }
    }


    //******************************************************************************************************************

}
