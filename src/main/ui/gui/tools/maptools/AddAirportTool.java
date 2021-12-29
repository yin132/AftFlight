package ui.gui.tools.maptools;

import model.Airport;
import model.Runway;
import ui.gui.PopupWindow;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// tool to add airport to map
public class AddAirportTool extends Tool {

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates an add airport tool
    public AddAirportTool(Tab controller) {
        super(controller, "Add Airport");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = PopupWindow.getNonEmptyString("enter the airport's name");
                if (mainController().getMap().getCheckpoint(name) != null) {
                    PopupWindow.showMessage("This name is already taken");
                    return;
                }
                String code = PopupWindow.getNonEmptyString("enter the airport code");
                int arrivalAltitude = PopupWindow.getInt("enter the airport's standard arrival altitude in ft",
                        0);
                List<Runway> runways = makeRunways();
                Airport airport = new Airport(name, new ArrayList<>(), code, arrivalAltitude, runways);
                mainController().getMap().addAirport(airport);
                mainController().update();
            }
        });
    }

    // EFFECTS: makes a list of runways
    private List<Runway> makeRunways() {
        List<Runway> runways = new ArrayList<>();
        List<String> options = new ArrayList<>();
        options.add("L");
        options.add("C");
        options.add("R");
        options.add("N");
        while (PopupWindow.getYesNo("Would you like to add another runway to this airport?")) {
            int runwayNumber = PopupWindow.getInt("enter the runway number", 1, 36);
            char runwayDesignator = PopupWindow.getString("enter the runway designator or N if there is none",
                    options).charAt(0);
            runways.add(new Runway(runwayNumber, runwayDesignator));
        }
        return runways;
    }
}
