package ui.gui.menus;

import model.FlightPlan;
import persistence.JsonReader;
import ui.gui.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

// Code obtained and modified from components-MenuDemoProject
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
// creates the menu for loading
public class LoadMenu extends Menu {

    // REQUIRES: controller is menu bar this belongs in
    // MODIFIES: this
    // EFFECTS: creates the load menu
    public LoadMenu(MenuBar controller) {
        super(controller,"Load");

        initMenuItem("Load Map");
        initMenuItem("Load Aircraft");
        initMenuItem("Load FlightPlan");
    }

    // REQUIRES: obj must be one of: "map" "aircraft" "flightPlan"
    // MODIFIES: controller in subController
    // EFFECTS: load map, aircraft, or flight plan from file
    private void doLoad(String obj) {
        try {
            JsonReader reader = new JsonReader("./data/" + obj + ".json");
            switch (obj) {
                case "map":
                    mainController().setFlightPlan(null);
                    mainController().setMap(reader.readMap());
                    break;
                case "aircraft":
                    mainController().setAircraft(reader.readAircraft());
                    break;
                default:
                    FlightPlan flightPlan = reader.readFlightPlan();
                    mainController().setFlightPlan(flightPlan);
                    mainController().setMap(flightPlan.getMap());
                    mainController().setAircraft(flightPlan.getAircraft());
                    break;
            }
            mainController().update();
            PopupWindow.showMessage(obj + " loaded");
        } catch (IOException e) {
            PopupWindow.showMessage("unable to load " + obj);
        }
    }

    // MODIFIES: controller in subController
    // EFFECTS: load object according to button press
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JMenuItem) e.getSource()).getText();
        String obj;
        switch (buttonText) {
            case "Load Map":
                obj = "map";
                break;
            case "Load Aircraft":
                obj = "aircraft";
                break;
            default:
                obj = "flightPlan";
                break;
        }
        doLoad(obj);
    }

    @Override
    public void update() {
        for (Component component : getMenuComponents()) {
            component.setVisible(true);
        }
    }
}
