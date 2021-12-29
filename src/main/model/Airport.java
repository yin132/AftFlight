package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// an airport on map which can act as a checkpoint with an ICAO code, runways, and arrival altitude
public class Airport extends Checkpoint {

    private String code;
    private int arrivalAlt;
    private List<Runway> runways;

    // MODIFIES: this
    // EFFECTS: constructor for an airport with name, legs, and an airport code
    public Airport(String name, List<Leg> legs, String code) {
        super(name, legs);
        this.code = code;
        this.arrivalAlt = 2000;
        this.runways = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: constructor for  an airport with name, legs, airport code, and arrival altitude
    public Airport(String name, List<Leg> legs, String code, int arrivalAlt) {
        super(name, legs);
        this.code = code;
        this.arrivalAlt = arrivalAlt;
        this.runways = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: constructor for an airport with name, legs, airport code. arrival alt, and runways
    public Airport(String name, List<Leg> legs, String code,
                   int arrivalAlt, List<Runway> runways) {
        super(name, legs);
        this.code = code;
        this.arrivalAlt = arrivalAlt;
        this.runways = runways;
    }

    // getters

    public String getCode() {
        return code;
    }

    public int getArrivalAlt() {
        return arrivalAlt;
    }

    public List<Runway> getRunways() {
        return runways;
    }

    // MODIFIES: this
    // EFFECTS: adds runway to runways if it is not already in runways, return true if successful
    public boolean addRunway(Runway runway) {
        String rn = runway.getName();
        for (Runway r : runways) {
            if (r.getName().equals(rn)) {
                return false;
            }
        }
        runways.add(runway);
        return true;
    }

    // JSON methods

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("code", code);
        json.put("arrivalAlt", arrivalAlt);
        json.put("runways", runwaysToJson());
        return json;
    }

    // EFFECTS: returns runways in this checkpoint as a JSON array
    private JSONArray runwaysToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Runway r : runways) {
            jsonArray.put(r.toJson());
        }
        return jsonArray;
    }
}
