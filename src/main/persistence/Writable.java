package persistence;

import org.json.JSONObject;

// Code obtained and modified from JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents an object that can be turned into a json to be written into a file
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
