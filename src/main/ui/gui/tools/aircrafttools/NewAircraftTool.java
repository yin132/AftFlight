package ui.gui.tools.aircrafttools;

import model.Aircraft;
import ui.gui.PopupWindow;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// tool to make new aircraft
public class NewAircraftTool extends Tool {

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates a newAircraft tool
    public NewAircraftTool(Tab controller) {
        super(controller, "New Aircraft");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (PopupWindow.getYesNo("Create a new aircraft?")) {
                    mainController().setAircraft(makeNewAircraft());
                }
                mainController().update();
            }
        });
    }

    // EFFECTS: asks the user for input and creates a new aircraft
    private Aircraft makeNewAircraft() {
        String registration = PopupWindow.getString("Enter the plane's registration");
        double fuel = PopupWindow.getDouble("Enter the plane's fuel", 0);
        double fuelBurn = PopupWindow.getDouble("Enter the plane's fuel burn per hour", 0);
        int cruiseSpeed = PopupWindow.getInt("Enter the plane's cruise speed in knots", 0);
        return new Aircraft(registration, fuel, fuelBurn, cruiseSpeed);
    }

}
