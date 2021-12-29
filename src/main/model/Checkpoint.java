package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.List;

// a checkpoint on a map with a name and legs to get to adjacent checkpoints
public class Checkpoint implements Writable {

    protected String name;
    protected List<Leg> legs;

    // MODIFIES: this
    // EFFECTS: constructor for checkpoint with a name and outgoing legs
    public Checkpoint(String name, List<Leg> legs) {
        this.name = name;
        this.legs = legs;
    }

    // getters

    public String getName() {
        return name;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    // EFFECTS: returns the leg with destination of given string, null if not found
    public Leg getLegTo(String destination) {
        for (Leg l : legs) {
            if (l.getDestName().equals(destination)) {
                return l;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: adds leg to legs if leg to same destination does not already exist, return true if successful
    public boolean addLeg(Leg newLeg) {
        String destination = newLeg.getDestName();
        for (Leg l : legs) {
            if (l.getDestName().equals(destination)) {
                return false;
            }
        }
        legs.add(newLeg);
        return true;
    }

    // JSON methods

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("legs", legsToJson());
        return json;
    }

    // EFFECTS: returns legs in this checkpoint as a JSON array
    private JSONArray legsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Leg l : legs) {
            jsonArray.put(l.toJson());
        }
        return jsonArray;
    }
}
