package model;

import org.json.JSONObject;
import persistence.Writable;

// an aircraft with a registration and its performance data
public class Aircraft implements Writable {
    private String registration;
    private double fuel;
    private double fuelBurn;
    private int cruiseSpeed;

    // REQUIRES: fuel, fuel burn, and cruise speed are positive
    // MODIFIES: this
    // EFFECTS: instantiates an aircraft with its performance data
    public Aircraft(String registration, double fuel, double fuelBurn, int cruiseSpeed) {
        this.registration = registration;
        this.fuel = fuel;
        this.fuelBurn = fuelBurn;
        this.cruiseSpeed = cruiseSpeed;
    }

    // getters

    public String getRegistration() {
        return registration;
    }

    public double getFuelBurn() {
        return fuelBurn;
    }

    public double getFuel() {
        return fuel;
    }

    public int getCruiseSpeed() {
        return cruiseSpeed;
    }

    // setters

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    // JSON methods

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("registration", registration);
        json.put("fuel", fuel);
        json.put("fuelBurn", fuelBurn);
        json.put("cruiseSpeed", cruiseSpeed);
        return json;
    }
}
