package model;

import org.json.JSONObject;
import persistence.Writable;

// a runway with its designator, and possibly dimensions and conditions
public class Runway implements Writable {

    private int number;
    private int heading;
    private char side;
    private int length;
    private int width;
    private char surface;
    private String notes;

    // REQUIRES: number is in [1, 36]
    // MODIFIES: this
    // EFFECTS: constructor for a runway given its number
    public Runway(int number) {
        initialize(number);
    }

    // REQUIRES: number is in [1, 36]
    // MODIFIES: this
    // EFFECTS: constructor for a parallel runway given its number
    public Runway(int number, char side) {
        if (side != 'L' && side != 'R' && side != 'C') {
            initialize(number);
        } else {
            initialize(number, side);
        }
    }

    // REQUIRES: number is in [1, 36], length and width are positive, surface is one of: H S U
    // MODIFIES: this
    // EFFECTS: constructor for a runway given its number, length, width, and surface
    public Runway(int number, int length, int width, char surface) {
        initialize(number);
        this.length = length;
        this.width = width;
        this.surface = surface;
    }

    // REQUIRES: number is in [1, 36], length and width are positive, surface is one of: H S
    // MODIFIES: this
    // EFFECTS: constructor for a parallel runway given its number, length, width, and surface
    public Runway(int number, char side, int length, int width, char surface) {
        if (side != 'L' && side != 'R' && side != 'C') {
            initialize(number);
        } else {
            initialize(number, side);
        }
        this.length = length;
        this.width = width;
        this.surface = surface;
    }

    // setters

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // getters

    public int getEastHeading() {
        return heading;
    }

    public int getWestHeading() {
        return heading + 180;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public String getNotes() {
        return notes;
    }

    // EFFECTS: returns the number and side of the eastern runway
    public String getEastName() {
        String rsf = String.valueOf(number);
        if (rsf.length() < 2) {
            rsf = "0" + number;
        }
        if (side == 0) {
            return rsf;
        } else {
            return rsf + side;
        }
    }

    // EFFECTS: returns the number and side of the western runway
    public String getWestName() {
        if (side == 0) {
            return String.valueOf(number + 18);
        } else {
            return String.valueOf(number + 18) + flip(side);
        }
    }

    // EFFECTS: returns the runway name
    public String getName() {
        String rsf = String.valueOf(number);
        if (rsf.length() < 2) {
            rsf = "0" + number;
        }
        if (side == 0) {
            return rsf + "/" + (number + 18);
        } else {
            return rsf + side + "/" + (number + 18) + flip(side);
        }
    }

    // EFFECTS: returns the conditions of the runway
    public String getCondition() {
        String condition = "";
        if (surface != 0) {
            condition += length + "x" + width + " " + surface;
        }
        if (notes != null && !notes.equals("")) {
            if (condition.length() > 0) {
                condition += " " + notes;
            } else {
                condition += notes;
            }
        }
        return condition;
    }

    // REQUIRES: number is in [1, 36]
    // MODIFIES: this
    // EFFECTS: initializes runway number and heading to be in [1, 18], returns true if flipped
    private boolean initialize(int number) {
        notes = "";
        if (number > 18) {
            number -= 18;
            this.number = number;
            this.heading = number * 10;
            return true;
        } else {
            this.number = number;
            this.heading = number * 10;
            return false;
        }
    }

    // REQUIRES: number is in [1, 36], char is one of: L C R
    // MODIFIES: this
    // EFFECTS: initializes runway number and heading to be in [1, 18] and side, returns true if flipped
    private void initialize(int number, char side) {
        if (initialize(number)) {
            this.side = flip(side);
        } else {
            this.side = side;
        }
    }

    // EFFECTS: flips the side
    private char flip(char side) {
        switch (side) {
            case 'L':
                side = 'R';
                break;
            case 'R':
                side = 'L';
                break;
            default:
                break;
        }
        return side;
    }

    // JSON methods

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("number", number);
        json.put("side", String.valueOf(side));
        json.put("length", length);
        json.put("width", width);
        json.put("surface", String.valueOf(surface));
        json.put("notes", notes);
        return json;
    }
}
