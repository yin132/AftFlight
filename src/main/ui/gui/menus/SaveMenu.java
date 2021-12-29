package ui.gui.menus;

import persistence.JsonWriter;
import ui.gui.PopupWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

// Code obtained and modified from components-MenuDemoProject
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
// creates the menu for saving
public class SaveMenu extends Menu {

    // REQUIRES: controller is menu bar this belongs in
    // MODIFIES: this
    // EFFECTS: creates the save menu
    public SaveMenu(MenuBar controller) {
        super(controller,"Save");

        initMenuItem("Save Map");
        initMenuItem("Save Aircraft");
        initMenuItem("Save FlightPlan");
    }

    // REQUIRES: obj must be one of: "map" "aircraft" "flightPlan"
    // EFFECTS: save map, aircraft, or flightplan to file
    private void doSave(String obj) {
        try {
            JsonWriter writer = new JsonWriter("./data/" + obj + ".json");
            writer.open();
            switch (obj) {
                case "map":
                    writer.write(mainController().getMap());
                    break;
                case "aircraft":
                    writer.write(mainController().getAircraft());
                    break;
                default:
                    writer.write(mainController().getFlightPlan());
                    break;
            }
            writer.close();
            PopupWindow.showMessage(obj + " saved");
        } catch (IOException e) {
            PopupWindow.showMessage("unable to save " + obj);
        }
    }

    // EFFECTS: save object according to button press
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JMenuItem) e.getSource()).getText();
        String obj;
        switch (buttonText) {
            case "Save Map":
                obj = "map";
                break;
            case "Save Aircraft":
                obj = "aircraft";
                break;
            default:
                obj = "flightPlan";
                break;
        }
        doSave(obj);
    }

    @Override
    public void update() {
        getMenuComponent(0).setVisible(mainController().getMap() != null);
        getMenuComponent(1).setVisible(mainController().getAircraft() != null);
        getMenuComponent(2).setVisible(mainController().getFlightPlan() != null);
    }
}
