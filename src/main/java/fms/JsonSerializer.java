package fms;

import com.google.gson.Gson;

/**
 * Class to deserialize from JSON to Java Code
 */
public class JsonSerializer {
    /**
     * Turn JSON into java object
     * @param value the JSON code
     * @param returnType The type of object you want the JSON code to be in Java
     * @param <T> The Java type
     * @return Object of the specified type parsed from the JSON code
     */
    public static <T> T deserialize(String value, Class<T> returnType){
        return (new Gson()).fromJson(value, returnType);
    }

    //example syntax:
    //MyDataType value = JsonSerializer.deserialize(jsonString, MyDataType.class);



    /**
     * Turn java into JSON
     * @param value The value you want to encode
     * @param javaObject The object you want to turn into JSON
     * @param <T>
     * @return The JSON equivalent of your Java object
     */
    public static <T> String serialize(T value, Class<T> javaObject){
        return(new Gson()).toJson(value, javaObject);
    }

    //String jsonString = sonSerializer.serialize(this, ClearResult.class);

}
