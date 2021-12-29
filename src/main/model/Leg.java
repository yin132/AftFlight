package model;

import org.json.JSONObject;
import persistence.Writable;

// a leg to a checkpoint with a heading and distance
public class Leg implements Writable {

    private Checkpoint destination;
    private int heading;
    private int distance;

    // REQUIRES: heading is in [1, 360], distance is positive
    // MODIFIES: this
    // EFFECTS: constructor for a leg to a checkpoint in heading direction, distance away
    public Leg(Checkpoint destination, int heading, int distance) {
        this.destination = destination;
        this.heading = heading;
        this.distance = distance;
    }

    // getters
    public Checkpoint getDestination() {
        return destination;
    }

    public int getHeading() {
        return heading;
    }

    public int getDistance() {
        return distance;
    }

    // EFFECTS: returns name of destination as string
    public String getDestName() {
        return destination.getName();
    }

    // JSON methods

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("destination", destination.getName());
        json.put("heading", heading);
        json.put("distance", distance);
        return json;
    }
}
