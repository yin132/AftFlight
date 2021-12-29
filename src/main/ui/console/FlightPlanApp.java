package ui.console;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// runs AftFlight console ui
public class FlightPlanApp {
    private Scanner input;
    private Map map;
    private Aircraft aircraft;
    private FlightPlan flightPlan;

    // MODIFIES: this
    // EFFECTS: starts the flightplan application
    public FlightPlanApp() {
        runFlightPlanApp();
    }

    // MODIFIES: this
    // EFFECTS: runs the flight plan app from main menu
    private void runFlightPlanApp() {
        String command;
        init();

        while (true) {
            displayOuterMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                break;
            }
            processOuterCommand(command);
        }
        System.out.println("\nHave a safe flight!");
    }

    // MODIFIES: this
    // EFFECTS: runs the map menu
    private void runMapMenu() {
        String command;

        while (true) {
            displayMapMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                break;
            }
            processMapCommand(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: runs the aircraft menu
    private void runAircraftMenu() {
        String command;
        if (aircraft == null) {
            doBuildAircraft();
        }

        while (true) {
            displayACMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                break;
            }
            processAircraftCommand(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: runs the flightplan menu
    private void runFPMenu() {
        String command;
        if (flightPlan == null) {
            doBuildFlightPlan();
        }

        while (true) {
            displayFPMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("c")) {
                doCloseFlightPlan();
                break;
            } else if (command.equals("q")) {
                break;
            }
            processFPCommand(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: runs the data menu
    private void runDataMenu() {
        String command;

        while (true) {
            displayDataMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                break;
            }
            processDataCommand(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes scanner and map
    private void init() {
        input = new Scanner(System.in);
        map = new Map("Lower Mainland", -16);
    }

    // EFFECTS: displays selections for outermost menu
    private void displayOuterMenu() {
        System.out.println("\nSelect from:");

        System.out.println("\tmap -> edit map");
        System.out.println("\tac -> edit or build aircraft");
        System.out.println("\tfp -> edit or build flight plan");
        System.out.println("\tsl -> save or load data");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays selections for map menu
    private void displayMapMenu() {
        System.out.println("\nSelect from:");

        System.out.println("\tac -> add checkpoint");
        System.out.println("\taa -> add airport");
        System.out.println("\tal -> add leg");
        System.out.println("\tpcp -> print checkpoints");
        System.out.println("\tpap -> print airports");
        System.out.println("\tpl -> print legs for a checkpoint");
        System.out.println("\tq -> back to main menu");
    }

    // EFFECTS: displays selections for aircraft menu
    private void displayACMenu() {
        System.out.println("\nSelect from:");

        System.out.println("\tp -> print aircraft stats");
        System.out.println("\tf -> edit aircraft's fuel");
        System.out.println("\tn -> make a new aircraft");
        System.out.println("\tq -> return to main menu");
    }

    // EFFECTS: displays selections for flightplan menu
    private void displayFPMenu() {
        System.out.println("\nSelect from:");

        System.out.println("\ter -> add en-route point");
        System.out.println("\tclp -> clear en-route points");
        System.out.println("\tpfp -> print flight plan");
        System.out.println("\tprp -> print route data");
        System.out.println("\tc -> close flight plan");
        System.out.println("\tq -> back to main menu");
    }

    // EFFECTS: displays selections for save or load data menu
    private void displayDataMenu() {
        System.out.println("\nSelect from:");

        System.out.println("\tlm -> load map");
        System.out.println("\tsm -> save map");
        System.out.println("\tla -> load aircraft");
        System.out.println("\tsa -> save aircraft");
        System.out.println("\tlf -> load flight plan");
        System.out.println("\tsf -> save flight plan");
        System.out.println("\tq -> back to main menu");
    }

    // EFFECTS: takes user into a sub menu from outermost menu
    private void processOuterCommand(String command) {
        switch (command) {
            case "map":
                runMapMenu();
                break;
            case "ac":
                runAircraftMenu();
                break;
            case "fp":
                if (aircraft == null) {
                    System.out.println("Please create an aircraft first");
                } else if (map.getAirports().size() < 1) {
                    System.out.println("Please add an airport first");
                } else {
                    runFPMenu();
                }
                break;
            case "sl":
                runDataMenu();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: processes command from map menu
    private void processMapCommand(String command) {
        switch (command) {
            case "ac":
                doAddCheckpoint();
                break;
            case "aa":
                doAddAirport();
                break;
            case "al":
                doAddLeg();
                break;
            case "pcp":
                doPrintCheckpoints();
                break;
            case "pap":
                doPrintAirports();
                break;
            case "pl":
                doPrintLegs();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: processes command from aircraft menu
    private void processAircraftCommand(String command) {
        switch (command) {
            case "p":
                doPrintAircraft();
                break;
            case "f":
                doEditAircraftFuel();
                break;
            case "n":
                doBuildAircraft();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: processes command from flightplan menu
    private void processFPCommand(String command) {
        switch (command) {
            case "er":
                doAddEnRoutePoint();
                break;
            case "clp":
                doClearEnRoutePoints();
                break;
            case "pfp":
                doPrintFlightPlan();
                break;
            case "prp":
                doPrintRawPlan();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: processes command from data menu
    private void processDataCommand(String command) {
        switch (command) {
            case "lm":
                doLoad("map");
                break;
            case "sm":
                doSave("map");
                break;
            case "la":
                doLoad("aircraft");
                break;
            case "sa":
                doSave("aircraft");
                break;
            case "lf":
                doLoad("flightPlan");
                break;
            case "sf":
                doSave("flightPlan");
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // REQUIRES: obj must be one of: "map" "aircraft" "flightPlan"
    // MODIFIES: this
    // EFFECTS: load map, aircraft, or flight plan from file
    private void doLoad(String obj) {
        try {
            JsonReader reader = new JsonReader("./data/" + obj + ".json");
            switch (obj) {
                case "map":
                    doCloseFlightPlan();
                    map = reader.readMap();
                    break;
                case "aircraft":
                    aircraft = reader.readAircraft();
                    break;
                default:
                    doCloseFlightPlan();
                    flightPlan = reader.readFlightPlan();
                    map = flightPlan.getMap();
                    aircraft = flightPlan.getAircraft();
                    break;
            }
            System.out.println(obj + " loaded");
        } catch (IOException e) {
            System.out.println("unable to load " + obj);
        }
    }

    // REQUIRES: obj must be one of: "map" "aircraft" "flightPlan"
    // EFFECTS: save map, aircraft, or flightplan to file
    private void doSave(String obj) {
        try {
            JsonWriter writer = new JsonWriter("./data/" + obj + ".json");
            writer.open();
            switch (obj) {
                case "map":
                    writer.write(map);
                    break;
                case "aircraft":
                    writer.write(aircraft);
                    break;
                default:
                    writer.write(flightPlan);
                    break;
            }
            writer.close();
            System.out.println(obj + " saved");
        } catch (IOException e) {
            System.out.println("unable to save " + obj);
        }
    }

    // MODIFIES: this
    // EFFECTS: create an aircraft
    private void doBuildAircraft() {
        String reg;
        double fuel;
        double fuelBurn;
        int cruiseSpeed;

        System.out.println("Aircraft registration: ");
        reg = input.next();
        System.out.println("Fuel: ");
        fuel = input.nextDouble();
        System.out.println("Fuel burn: ");
        fuelBurn = input.nextDouble();
        System.out.println("Cruise speed: ");
        cruiseSpeed = input.nextInt();

        aircraft = new Aircraft(reg, fuel, fuelBurn, cruiseSpeed);
        System.out.println("Aircraft " + aircraft.getRegistration() + " added");
    }

    // MODIFIES: this
    // EFFECTS: create a flight plan
    private void doBuildFlightPlan() {
        Airport dep;
        Airport dest;
        System.out.println("Select the departure airport: ");
        dep = selectAirport();
        System.out.println("Select the destination airport: ");
        dest = selectAirport();
        flightPlan = new FlightPlan(aircraft, map, dep.getCode(), dest.getCode());
        System.out.print("Started a flight plan with " + aircraft.getRegistration() + " in " + map.getRegion());
        System.out.println(" from " + dep.getName() + " to " + dest.getName());
    }

    // EFFECTS: print out specs of the aircraft
    private void doPrintAircraft() {
        System.out.println("Aircraft registration: " + aircraft.getRegistration());
        System.out.println("Fuel: " + aircraft.getFuel());
        System.out.println("Fuel burn: " + aircraft.getFuelBurn());
        System.out.println("Cruise speed: " + aircraft.getCruiseSpeed());
    }

    // MODIFIES: this
    // EFFECTS: changes aircraft's fuel
    private void doEditAircraftFuel() {
        System.out.println("New fuel: ");
        aircraft.setFuel(input.nextDouble());
        System.out.println("Updated aircraft fuel to " + aircraft.getFuel());
    }

    // MODIFIES: this
    // EFFECTS: adds a checkpoint to map
    private void doAddCheckpoint() {
        System.out.println("Checkpoint name: ");
        String name = getNextLine();
        List<Leg> legs = makeLegs();
        System.out.println(legs.size() + " legs added for " + name);

        map.addCheckpoint(new Checkpoint(name, legs));
        System.out.println("New checkpoint " + map.getCheckpoint(name).getName() + " added");
    }

    // MODIFIES: this
    // EFFECTS: adds an airport to map
    private void doAddAirport() {
        System.out.println("Airport name:");
        String name = getNextLine();
        System.out.println("Airport code:");
        String code = input.next();

        List<Leg> legs = makeLegs();
        System.out.println(legs.size() + " legs added for " + name);
        System.out.println("Set an arrival altitude? [y/n]");

        if (yesOrNo()) {
            System.out.println("Arrival altitude: ");
            map.addAirport(new Airport(name, legs, code, input.nextInt()));
        } else {
            map.addAirport(new Airport(name, legs, code));
        }
        System.out.println("New airport " + map.getAirport(code).getName() + " "
                + map.getAirport(code).getCode() + " added");
    }

    // MODIFIES: this
    // EFFECTS: add a leg to map
    private void doAddLeg() {
        System.out.println("select the checkpoint to add legs to");
        Checkpoint cp = selectCheckpoint();
        Leg l = makeLeg();
        map.addLegTo(cp.getName(), l);
        System.out.println("Leg added between " + cp.getName() + " and " + l.getDestName());
    }

    // EFFECTS: print out all checkpoints in map
    private void doPrintCheckpoints() {
        System.out.println("Available checkpoints:");
        for (Checkpoint cp : map.getCheckpoints()) {
            System.out.println(cp.getName());
        }
    }

    // EFFECTS: print out all airports in map
    private void doPrintAirports() {
        System.out.println("Available airports: ");
        for (Airport ap : map.getAirports()) {
            System.out.println(ap.getName() + " " + ap.getCode());
        }
    }

    // EFFECTS: print out all legs of a checkpoint in map
    private void doPrintLegs() {
        System.out.println("Select which checkpoint's legs to look at");
        Checkpoint cp = selectCheckpoint();
        for (Leg l : cp.getLegs()) {
            System.out.print("to: " + l.getDestName() + ", heading: " + l.getHeading());
            System.out.println(", distance: " + l.getDistance());
        }
    }

    // MODIFIES: this
    // EFFECTS: adds an en-route point to flight plan
    private void doAddEnRoutePoint() {
        System.out.println("select the checkpoint to add as an en-route point");
        Checkpoint cp = selectCheckpoint();
        flightPlan.addEnRoutePoint(cp.getName());
        System.out.println(cp.getName() + " added as an en-route point");
    }

    // MODIFIES: this
    // EFFECTS: clears all en-route points from flight plan
    private void doClearEnRoutePoints() {
        flightPlan.removeEnRoutePoints();
        System.out.println("En-route points removed from flight plan");
    }

    // EFFECTS: prints out flight plan with corrected headings, ETEs, and fuel burns
    private void doPrintFlightPlan() {
        List<String> checkpoints = flightPlan.getCpNames();
        List<Integer> headings = flightPlan.calculateHeadings();
        List<Double> ete = flightPlan.calculateETEs();
        List<Double> fuelBruns = flightPlan.calculateFuelBurns();

        System.out.println("Flight plan for " + flightPlan.getAircraft().getRegistration() + " in " + map.getRegion());
        for (int k = 0; k < headings.size(); k++) {
            System.out.println(checkpoints.get(k));
            System.out.print("\tHeading: " + headings.get(k));
            System.out.print(", ETE: " + Math.round(ete.get(k)));
            System.out.println(", Fuel: " + fuelBruns.get(k));
        }
        System.out.println(checkpoints.get(checkpoints.size() - 1));
    }

    // EFFECTS: prints out true headings and distances in flight plan
    private void doPrintRawPlan() {
        List<String> checkpoints = flightPlan.getCpNames();
        List<Integer> distances = flightPlan.getDistances();
        List<Integer> headings = flightPlan.getHeadings();

        System.out.println("Flight plan route data in " + map.getRegion());
        for (int k = 0; k < headings.size(); k++) {
            System.out.println(checkpoints.get(k));
            System.out.print("\tHeading: " + headings.get(k));
            System.out.println(", Distance: " + distances.get(k));
        }
        System.out.println(checkpoints.get(checkpoints.size() - 1));
    }

    // MODIFIES: this
    // EFFECTS: delete flight plan
    private void doCloseFlightPlan() {
        flightPlan = null;
        System.out.println("Closed flight plan");
    }

    // EFFECTS: makes a new leg
    private Leg makeLeg() {
        int heading = -1;
        int distance = -1;
        Checkpoint cp;

        System.out.println("choose the checkpoint this leg goes to");
        cp = selectCheckpoint();
        while (heading < 1 || heading > 360) {
            System.out.println("Heading to get there: ");
            heading = input.nextInt();
        }
        while (distance < 0) {
            System.out.println("Distance to get there: ");
            distance = input.nextInt();
        }

        System.out.print("Making new leg to " + cp.getName());
        System.out.println(" on heading " + heading + ", " + distance + "nm away");
        return new Leg(cp, heading, distance);
    }

    // EFFECTS: selects a checkpoint from checkpoints in map
    private Checkpoint selectCheckpoint() {
        Checkpoint cp;
        while (true) {
            doPrintCheckpoints();
            System.out.println("Select a checkpoint: ");
            cp = map.getCheckpoint(getNextLine());
            if (cp == null) {
                continue;
            }
            System.out.println("Is " + cp.getName() + " correct? [y/n]");
            if (yesOrNo()) {
                break;
            }
        }
        return cp;
    }

    // EFFECTS: makes and returns a list of legs
    private List<Leg> makeLegs() {
        List<Leg> legs = new ArrayList<>();
        while (true) {
            System.out.println("Add another leg? [y/n]");
            if (!yesOrNo()) {
                break;
            }
            legs.add(makeLeg());
        }
        return legs;
    }

    // EFFECTS: selects an airport from airports in map
    private Airport selectAirport() {
        Airport ap;
        while (true) {
            doPrintAirports();
            System.out.println("Select an airport's code: ");
            ap = map.getAirport(getNextLine());
            System.out.println("Is " + ap.getName() + " correct? [y/n]");
            if (yesOrNo()) {
                break;
            }
        }
        return ap;
    }

    // EFFECTS: returns true for y and false for n
    private Boolean yesOrNo() {
        String select;
        while (true) {
            select = input.next();
            select = select.toLowerCase();
            if (select.equals("y")) {
                return true;
            } else if (select.equals("n")) {
                return false;
            }
            System.out.println("Enter \"y\" for yes, or \"n\" for no:");
        }
    }

    // EFFECTS: reads the next line as string safely
    private String getNextLine() {
        String buffer = input.nextLine();
        if (buffer.equals("")) {
            buffer = input.nextLine();
        }
        return buffer;
    }
}
