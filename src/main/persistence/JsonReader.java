package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


// Code obtained and modified from JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents a reader that reads writables from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads flight plan from file and returns it;
    // throws IOException if an error occurs reading data from file
    public FlightPlan readFlightPlan() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFlightPlan(jsonObject);
    }

    // EFFECTS: reads aircraft from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Aircraft readAircraft() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAircraft(jsonObject);
    }

    // EFFECTS: reads map from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Map readMap() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMap(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses flight plan from JSON object and returns it
    private FlightPlan parseFlightPlan(JSONObject jsonObject) {
        Map map = parseMap(jsonObject.getJSONObject("map"));
        Aircraft aircraft = parseAircraft(jsonObject.getJSONObject("aircraft"));
        String departure = jsonObject.getString("departure");
        String destination = jsonObject.getString("destination");
        FlightPlan flightPlan = new FlightPlan(aircraft, map, departure, destination);

        JSONArray jsonArray = jsonObject.getJSONArray("enRoutePoints");
        for (Object s : jsonArray) {
            String nextPoint = (String) s;
            flightPlan.addEnRoutePoint(nextPoint);
        }
        return flightPlan;
    }

    // EFFECTS: parses aircraft from JSON object and returns it
    private Aircraft parseAircraft(JSONObject jsonObject) {
        String registration = jsonObject.getString("registration");
        double fuel = jsonObject.getDouble("fuel");
        double fuelBurn = jsonObject.getDouble("fuelBurn");
        int cruiseSpeed = jsonObject.getInt("cruiseSpeed");
        return new Aircraft(registration, fuel, fuelBurn, cruiseSpeed);
    }

    // EFFECTS: parses map from JSON object and returns it
    private Map parseMap(JSONObject jsonObject) {
        String region = jsonObject.getString("region");
        int magVar = jsonObject.getInt("magVar");
        Map map = new Map(region, magVar);
        List<JSONArray> loLegs = new ArrayList<>();
        addCheckpoints(map, jsonObject, loLegs);
        addLegs(map, loLegs);
        return map;
    }

    // MODIFIES: map, loLegs
    // EFFECTS: parses checkpoints from JSON object and adds them to map, adds lists of legs to List of Legs
    private void addCheckpoints(Map map, JSONObject jsonObject, List<JSONArray> loLegs) {
        JSONArray jsonArray = jsonObject.getJSONArray("checkpoints");
        for (Object json : jsonArray) {
            JSONObject nextCheckpoint = (JSONObject) json;
            addCheckpoint(map, nextCheckpoint, loLegs);
        }
    }

    // MODIFIES: map, loLegs
    // EFFECTS: parses checkpoint or airport from JSON object and adds it to map, and adds legs to List of Legs
    private void addCheckpoint(Map map, JSONObject jsonObject, List<JSONArray> loLegs) {
        String name = jsonObject.getString("name");
        Checkpoint checkpoint = new Checkpoint(name, new ArrayList<>());
        boolean isAirport;

        try {
            jsonObject.getString("code");
            isAirport = true;
        } catch (Exception e) {
            isAirport = false;
        }

        if (isAirport) {
            Airport airport = parseAirport(jsonObject);
            map.addAirport(airport);
        } else {
            map.addCheckpoint(checkpoint);
        }

        JSONArray jsonArray = jsonObject.getJSONArray("legs");
        loLegs.add(jsonArray);
    }

    // REQUIRES: lists of legs are in same order and length as checkpoints in map,
    //              and all destination checkpoints are in map
    // MODIFIES: map
    // EFFECTS: parses legs from JSONArray and adds them to corresponding checkpoints in map
    private void addLegs(Map map, List<JSONArray> loLegs) {
        List<Checkpoint> checkpoints = map.getCheckpoints();
        for (int k = 0; k < checkpoints.size(); k++) {
            String departure = checkpoints.get(k).getName();
            for (Object json : loLegs.get(k)) {
                JSONObject jsonObject = (JSONObject) json;
                String destName = jsonObject.getString("destination");
                Checkpoint destination = map.getCheckpoint(destName);
                int heading = jsonObject.getInt("heading");
                int distance = jsonObject.getInt("distance");
                Leg nextLeg = new Leg(destination, heading, distance);
                map.addLegTo(departure, nextLeg);
            }
        }
    }

//    // EFFECTS: parses and returns checkpoint or airport
//    private Checkpoint parseCheckpoint(JSONObject jsonObject) {
//        String name = jsonObject.getString("name");
//        Checkpoint checkpoint = new Checkpoint(name, new ArrayList<>());
//        boolean isAirport;
//
//        try {
//            jsonObject.getString("code");
//            isAirport = true;
//        } catch (Exception e) {
//            isAirport = false;
//        }
//
//        if (isAirport) {
//            return parseAirport(jsonObject);
//        } else {
//            return checkpoint;
//        }
//    }

    // EFFECTS: parses and returns airport
    private Airport parseAirport(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String code = jsonObject.getString("code");
        int arrivalAlt = jsonObject.getInt("arrivalAlt");
        List<Runway> runways = parseRunways(jsonObject.getJSONArray("runways"));
        return new Airport(name, new ArrayList<>(), code, arrivalAlt, runways);
    }

    // EFFECTS: parses and returns runways
    private List<Runway> parseRunways(JSONArray jsonArray) {
        List<Runway> runways = new ArrayList<>();
        for (Object json : jsonArray) {
            runways.add(parseRunway((JSONObject) json));
        }
        return runways;
    }

    // EFFECTS: parses and returns runway
    private Runway parseRunway(JSONObject jsonObject) {
        Runway runway;
        int number = jsonObject.getInt("number");
        char side = jsonObject.getString("side").charAt(0);
        int length = jsonObject.getInt("length");
        int width = jsonObject.getInt("width");
        char surface = jsonObject.getString("surface").charAt(0);
        String notes = jsonObject.getString("notes");
        if (length <= 0) {
            runway = new Runway(number, side);
        } else {
            runway = new Runway(number, side, length, width, surface);
        }
        runway.setNotes(notes);
        return runway;
    }
}
