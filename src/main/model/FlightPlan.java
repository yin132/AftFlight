package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// a flight plan route for a region and aircraft to and from two airports
public class FlightPlan implements Writable {

    private Aircraft aircraft;
    private Map map;
    private Airport departure;
    private Airport destination;
    private List<String> cpNames;
    private List<Leg> legs;
    private List<String> enRoutePoints;

    // REQUIRES: departure and destination are codes of airports in map
    // MODIFIES: this
    // EFFECTS: initializes flight plan
    public FlightPlan(Aircraft aircraft, Map map, String departureCode, String destinationCode) {
        this.aircraft = aircraft;
        this.map = map;
        this.departure = map.getAirport(departureCode);
        this.destination = map.getAirport(destinationCode);
        enRoutePoints = new ArrayList<>();
        calculateRoute();
    }

    // getters

    public Airport getDeparture() {
        return departure;
    }

    public Airport getDestination() {
        return destination;
    }

    public List<String> getCpNames() {
        return cpNames;
    }

    public List<String> getEnRoutePoints() {
        return enRoutePoints;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public Map getMap() {
        return map;
    }

    // setters

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    // MODIFIES: this
    // EFFECTS: finds a route from departure to destination that passes all en-route points in order
    public void calculateRoute() {
        List<String> newRoute;
        try {
            newRoute = new ArrayList<>();
            if (enRoutePoints.size() == 0) {
                newRoute = map.findRoute(departure.getName(), destination.getName());
            } else {
                List<String> segment;
                segment = map.findRoute(departure.getName(), enRoutePoints.get(0));
                segment.remove(segment.size() - 1);
                newRoute.addAll(segment);
                for (int k = 1; k < enRoutePoints.size(); k++) {
                    segment = map.findRoute(enRoutePoints.get(k - 1), enRoutePoints.get(k));
                    segment.remove(segment.size() - 1);
                    newRoute.addAll(segment);
                }
                segment = map.findRoute(enRoutePoints.get(enRoutePoints.size() - 1), destination.getName());
                newRoute.addAll(segment);
            }

            cpNames = newRoute;
            legs = map.getLegsBetween(cpNames);
        } catch (NullPointerException e) {
            throw new NullPointerException("could not calculate route");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds checkpoint with name as an en-route point and recalculate route,
    //          or do nothing is checkpoint is already farthest checkpoint,
    //          throws null pointer exception if unreachable
    public void addEnRoutePoint(String name) {
        if (enRoutePoints.size() != 0 && enRoutePoints.get(enRoutePoints.size() - 1).equals(name)) {
            return;
        } else {
            List<String> check = new ArrayList<>(enRoutePoints);
            enRoutePoints.add(name);
            try {
                calculateRoute();
            } catch (NullPointerException e) {
                enRoutePoints = check;
                throw new NullPointerException();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes all en-route points and recalculates route
    public void removeEnRoutePoints() {
        enRoutePoints.clear();
        calculateRoute();
    }

    // EFFECTS: return list of raw headings along route
    public List<Integer> getHeadings() {
        List<Integer> rsf = new ArrayList<>();
        for (Leg l : legs) {
            rsf.add(l.getHeading());
        }
        return rsf;
    }

    // EFFECTS: return list of raw distances along route
    public List<Integer> getDistances() {
        List<Integer> rsf = new ArrayList<>();
        for (Leg l : legs) {
            rsf.add(l.getDistance());
        }
        return rsf;
    }

    // EFFECTS: return list of corrected headings along route
    public List<Integer> calculateHeadings() {
        List<Integer> rsf = new ArrayList<>();
        int magVar = map.getMagVar();
        int next;
        for (Leg l : legs) {
            next = l.getHeading() + magVar;
            if (next < 1) {
                next += 360;
            } else if (next > 360) {
                next -= 360;
            }
            rsf.add(next);
        }
        return rsf;
    }

    // EFFECTS: return list of ETEs along route in minutes, rounded to nearest minute
    public List<Double> calculateETEs() {
        List<Double> rsf = new ArrayList<>();
        for (Leg l : legs) {
            rsf.add((double) Math.round((double) l.getDistance() * 600.0 / (double) aircraft.getCruiseSpeed()) / 10.0);
        }
        return rsf;
    }

    // EFFECTS: return list of ETEs along route in minutes, rounded up to next tenth
    public List<Double> calculateFuelBurns() {
        List<Double> rsf = new ArrayList<>();
        List<Double> etes = calculateETEs();
        for (Double ete : etes) {
            rsf.add((double) Math.round((aircraft.getFuelBurn() * ete / 6.0) + 0.4) / 10.0);
        }
        return rsf;
    }

    // JSON methods

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("map", map.toJson());
        json.put("aircraft", aircraft.toJson());
        json.put("departure", departure.getCode());
        json.put("destination", destination.getCode());
        json.put("enRoutePoints", enRoutePointsToJson());
        return json;
    }

    // EFFECTS: returns enRoutePoints as a JSON array
    private JSONArray enRoutePointsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (String s : enRoutePoints) {
            jsonArray.put(s);
        }
        return jsonArray;
    }
}
