package ui.gui.tabs;

import model.Airport;
import model.Checkpoint;
import model.FlightPlan;
import ui.gui.GUI;
import ui.gui.tools.flightplantools.AddEnRoutePointTool;
import ui.gui.tools.flightplantools.NewFlightPlanTool;
import ui.gui.tools.flightplantools.RemoveEnRoutePointsTool;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static ui.gui.GUI.NLINE;

// Code obtained and modified from SmartHome and CompositeDrawingEditorComplete
// https://github.com/UBCx-Software-Construction/long-form-problem-starters.git
// https://github.students.cs.ubc.ca/CPSC210/CompositeDrawingEditorComplete.git
// a FlightPlan tab
public class FlightPlanTab extends Tab {

    JTextArea planDisplay;
    JList enRoutePointsList;
    JList departure;
    JList destination;

    // MODIFIES: this
    // EFFECTS: makes flight plan tab
    public FlightPlanTab(GUI controller) {
        super(controller);
    }

//    // EFFECTS: returns the selected en-route point
//    public Checkpoint getSelectedPoint() {
//        return controller.getMap().getCheckpoint((String) enRoutePointsList.getSelectedValue());
//    }

    // EFFECTS: returns the selected departure airport
    public Airport getSelectedDeparture() {
        return (Airport) controller.getMap().getCheckpoint((String) departure.getSelectedValue());
    }

    // EFFECTS: returns the selected destination airport
    public Airport getSelectedDestination() {
        return (Airport) controller.getMap().getCheckpoint((String) destination.getSelectedValue());
    }

    @Override
    protected void placeToolButtons() {
        NewFlightPlanTool newFlightPlanTool = new NewFlightPlanTool(this);
        tools.put("new flight plan", newFlightPlanTool);
        toolArea.add(newFlightPlanTool.getButton());

        AddEnRoutePointTool addEnroutePointTool = new AddEnRoutePointTool(this);
        tools.put("add en-route point", addEnroutePointTool);
        toolArea.add(addEnroutePointTool.getButton());

        RemoveEnRoutePointsTool removeEnRoutePointsTool = new RemoveEnRoutePointsTool(this);
        tools.put("remove en-route points", removeEnRoutePointsTool);
        toolArea.add(removeEnRoutePointsTool.getButton());
    }

    @Override
    protected void initInfo() {
        infoArea = new JPanel();
        infoArea.setLayout(new GridLayout(0, 2));
        infoArea.setVisible(true);

        planDisplay = new JTextArea();
        planDisplay.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        planDisplay.setEditable(false);

        enRoutePointsList = new JList();
        enRoutePointsList.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        enRoutePointsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enRoutePointsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        departure = new JList();
        departure.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        departure.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        departure.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        destination = new JList();
        destination.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        destination.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        destination.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        add(infoArea, infoConstraints());
    }

    @Override
    protected void placeInfo() {
        infoArea.removeAll();
        if (isFlightplanActive()) {
            updatePlanDisplay();
            updateEnRoutePoints();
            infoArea.add(planDisplay);
            infoArea.add(enRoutePointsList);
        } else if (readyToFile()) {
            prepareToFile();
            infoArea.add(departure);
            infoArea.add(destination);
        } else {
            clearDisplay();
            infoArea.add(planDisplay);
        }
    }

    @Override
    public void update() {
        super.update();
        if (readyToFile()) {
            tools.get("new flight plan").activate();
        } else {
            tools.get("new flight plan").deactivate();
        }

        if (canAddPoint()) {
            tools.get("add en-route point").activate();
        } else {
            tools.get("add en-route point").deactivate();
        }

        if (hasEnRoutePoint()) {
            tools.get("remove en-route points").activate();
        } else {
            tools.get("remove en-route points").deactivate();
        }
    }

    // REQUIRES: flightplan is not null
    // MODIFIES: this
    // EFFECTS: updates the flight plan route data display
    private void updatePlanDisplay() {
        planDisplay.setText("");

        FlightPlan flightPlan = controller.getFlightPlan();
        List<String> checkpoints = flightPlan.getCpNames();
        List<Integer> magHeadings = flightPlan.calculateHeadings();
        List<Double> ete = flightPlan.calculateETEs();
        List<Double> fuelBruns = flightPlan.calculateFuelBurns();
        List<Integer> distances = flightPlan.getDistances();
        List<Integer> trueHeadings = flightPlan.getHeadings();

        planDisplay.append("Flight plan for " + flightPlan.getAircraft().getRegistration() + NLINE);
        planDisplay.append(checkpoints.get(0) + NLINE);
        for (int k = 0; k < ete.size(); k++) {
            planDisplay.append("\tTrue Heading: " + trueHeadings.get(k));
            planDisplay.append(", Mag Heading: " + magHeadings.get(k));
            planDisplay.append(", ETE: " + Math.round(ete.get(k)));
            planDisplay.append(", Distance: " + distances.get(k));
            planDisplay.append(", Fuel: " + fuelBruns.get(k) + NLINE);
            planDisplay.append(checkpoints.get(k + 1) + NLINE);
        }
    }

    // REQUIRES: flightplan is not null
    // MODIFIES: this
    // EFFECTS: updates the en-route points display
    private void updateEnRoutePoints() {
        enRoutePointsList.removeAll();
        DefaultListModel<String> checkpointNames = new DefaultListModel<>();
        checkpointNames.addElement("En-route points:");
        for (String cp : controller.getFlightPlan().getCpNames()) {
            checkpointNames.addElement(cp);
        }
        enRoutePointsList.setModel(checkpointNames);
    }

    // MODIFIES: this
    // EFFECTS: clears the display for when there is no active flight plan and one cannot be filed
    private void clearDisplay() {
        planDisplay.setText("Have a map with an airport and an aircraft to file a flight plan");
        enRoutePointsList.removeAll();
    }

    // MODIFIES: this
    // EFFECTS: updates the displays for filing a new flight plan
    private void prepareToFile() {
        List<Airport> airports = controller.getMap().getAirports();
        departure.removeAll();
        destination.removeAll();

        DefaultListModel<String> departureNames = new DefaultListModel<>();
        DefaultListModel<String> destinationNames = new DefaultListModel<>();
        departureNames.addElement("Departure airport:");
        destinationNames.addElement("Destination airport:");
        for (Airport ap : airports) {
            departureNames.addElement(ap.getName());
            destinationNames.addElement(ap.getName());
        }
        departure.setModel(departureNames);
        destination.setModel(destinationNames);
    }

    // EFFECTS: returns true if there is an active flightplan
    public boolean isFlightplanActive() {
        return controller.getFlightPlan() != null;
    }

    // EFFECTS: returns true if there is at least one en-route point in flightplan
    private boolean hasEnRoutePoint() {
        if (isFlightplanActive()) {
            return controller.getFlightPlan().getEnRoutePoints().size() >= 1;
        } else {
            return false;
        }
    }

    // EFFECTS: returns true if there is a map with an airport and aircraft loaded
    private boolean readyToFile() {
        if (controller.getMap() == null) {
            return false;
        } else {
            return controller.getMap().getAirports().size() >= 1 && controller.getAircraft() != null;
        }
    }

    // EFFECTS: returns true when an en-route point can be added
    private boolean canAddPoint() {
        if (isFlightplanActive()) {
            return controller.getMap().getCheckpoints().size() >= 2;
        }
        return false;
    }

}
