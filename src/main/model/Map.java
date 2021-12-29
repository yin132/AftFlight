package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// a map of a region with checkpoints, airports, and a magnetic variation
public class Map implements Writable {

    private String region;
    private int magVar;
    private List<Checkpoint> checkpoints;
    private List<Airport> airports;

    // REQUIRES: magVar is in [-359, 359], region is not null
    // MODIFIES: this
    // EFFECTS: creates a new empty map
    public Map(String region, int magVar) {
        this.region = region;
        this.magVar = magVar;
        this.checkpoints = new ArrayList<>();
        this.airports = new ArrayList<>();
    }

    // getters
    public String getRegion() {
        return region;
    }

    public int getMagVar() {
        return magVar;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    // EFFECTS: returns checkpoint with given name or null if checkpoint does not exist
    public Checkpoint getCheckpoint(String name) {
        if (name != null) {
            for (Checkpoint c : checkpoints) {
                if (name.equals(c.getName())) {
                    return c;
                }
            }
        }
        return null;
    }

    // EFFECTS: returns airport with given code, or null if airport does not exist
    public Airport getAirport(String code) {
        if (code != null) {
            for (Airport a : airports) {
                if (code.equals(a.getCode())) {
                    return a;
                }
            }
        }
        return null;
    }

    // REQUIRES: all destinations of checkpoint are in checkpoints, checkpoint is not null
    // MODIFIES: this
    // EFFECTS: adds checkpoint to checkpoints if it does not already exist,
    //          creates return legs from destinations
    public void addCheckpoint(Checkpoint checkpoint) {
        if (getCheckpoint(checkpoint.getName()) == null) {
            checkpoints.add(checkpoint);
            addReturnLegs(checkpoint.getLegs(), checkpoint);
        }
    }

    // REQUIRES: all destinations of airport are in checkpoints, airport is not null
    // MODIFIES: this
    // EFFECTS: adds airport to airports and checkpoints if it does not already exist,
    //          creates return legs from destinations
    public void addAirport(Airport airport) {
        if (getCheckpoint(airport.getName()) == null) {
            checkpoints.add(airport);
            airports.add(airport);
            addReturnLegs(airport.getLegs(), airport);
        }
    }

    // REQUIRES: departure and destination of leg be in checkpoints
    // MODIFIES: this
    // EFFECTS: makes and add leg between checkpoints and its return leg
    public void addLegTo(String departure, Leg leg) {
        Checkpoint depCP = getCheckpoint(departure);
        depCP.addLeg(leg);
        leg.getDestination().addLeg(new Leg(depCP, reciprocal(leg.getHeading()), leg.getDistance()));
    }

    // REQUIRES: departure is name of of Checkpoint in checkpoints, no arguments are null
    // EFFECTS: returns in order the names of checkpoints
    //          on shortest route between and including departure and destination,
    //          or null if cannot reach or find destination
    public List<String> findRoute(String departure, String destination) {
        return findRouteForCP(getCheckpoint(departure), new ArrayList<>(), 0,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), Integer.MAX_VALUE, destination);
    }

    // REQUIRES: route only has names of checkpoints in checkpoints and is not null
    // EFFECTS: returns the legs between the checkpoints
    public List<Leg> getLegsBetween(List<String> route) {
        List<Leg> rsf = new ArrayList<>();
        for (int k = 0; k < route.size() - 1; k++) {
            Checkpoint c = getCheckpoint(route.get(k));
            rsf.add(c.getLegTo(route.get(k + 1)));
        }
        return rsf;
    }

    // helpers

    // REQUIRES: all destinations of legs are Checkpoints in checkpoints, no arguments are null
    // MODIFIES: Checkpoints in this.checkpoints
    // EFFECTS: adds the return legs back to departure point for each leg
    private void addReturnLegs(List<Leg> outbound, Checkpoint departure) {
        for (Leg l : outbound) {
            l.getDestination().addLeg(new Leg(departure, reciprocal(l.getHeading()), l.getDistance()));
        }
    }

    // REQUIRES: given heading is in [1, 360]
    // EFFECTS: returns the reciprocal heading
    private int reciprocal(int heading) {
        if (heading > 180) {
            return heading - 180;
        } else {
            return heading + 180;
        }
    }

    // REQUIRES: all worklists are of the same length
    // EFFECTS: returns the shortest path to destination
    private List<String> findRouteForCP(Checkpoint cp, List<String> visited, int distance,
                                        List<Leg> wlLegs, List<List<String>> wlVisited, List<Integer> wlDistance,
                                        List<String> shortestPath, int shortestDistance, String destination) {
        if (destination.equals(cp.getName())) {
            if (distance < shortestDistance) {
                List<String> newShortest = new ArrayList<>(visited);
                newShortest.add(destination);
                return findRouteForLoCP(wlLegs, wlVisited, wlDistance, newShortest, distance, destination);
            } else {
                return findRouteForLoCP(wlLegs, wlVisited, wlDistance, shortestPath, shortestDistance, destination);
            }
        } else if (visited.contains(cp.getName())) {
            return findRouteForLoCP(wlLegs, wlVisited, wlDistance, shortestPath, shortestDistance, destination);
        } else {
            List<Leg> newWll = new ArrayList<>(wlLegs);
            List<List<String>> newWlv = new ArrayList<>(wlVisited);
            List<Integer> newWld = new ArrayList<>(wlDistance);
            List<String> nextV = new ArrayList<>(visited);
            nextV.add(cp.getName());
            for (Leg l : cp.getLegs()) {
                newWll.add(0, l);
                newWlv.add(0, nextV);
                newWld.add(0, distance + l.getDistance());
            }
            return findRouteForLoCP(newWll, newWlv, newWld, shortestPath, shortestDistance, destination);
        }
    }

    // REQUIRES: all worklists are of the same length
    // EFFECTS: returns the shortest path to destination
    private List<String> findRouteForLoCP(List<Leg> wlLegs, List<List<String>> wlVisited, List<Integer> wlDistance,
                                          List<String> shortestPath, int shortestDistance, String destination) {
        if (wlLegs.size() == 0) {
            if (shortestDistance >= Integer.MAX_VALUE) {
                return null;
            } else {
                return shortestPath;
            }
        } else {
            List<Leg> nextLegs = new ArrayList<>(wlLegs);
            List<List<String>> nextV = new ArrayList<>(wlVisited);
            List<Integer> nextD = new ArrayList<>(wlDistance);
            nextLegs.remove(0);
            nextV.remove(0);
            nextD.remove(0);
            return findRouteForCP(wlLegs.get(0).getDestination(), wlVisited.get(0), wlDistance.get(0),
                    nextLegs, nextV, nextD, shortestPath, shortestDistance, destination);
        }
    }

    // JSON methods

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("region", region);
        json.put("magVar", magVar);
        json.put("checkpoints", checkpointsToJson());
        return json;
    }

    // EFFECTS: returns checkpoints in this map as a JSON array
    private JSONArray checkpointsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Checkpoint c : checkpoints) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }
}
