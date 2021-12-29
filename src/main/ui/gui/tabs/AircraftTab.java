package ui.gui.tabs;

import model.Aircraft;
import ui.gui.GUI;
import ui.gui.tools.aircrafttools.EditFuelTool;
import ui.gui.tools.aircrafttools.NewAircraftTool;

import javax.swing.*;

import java.awt.*;

import static ui.gui.GUI.NLINE;

// Code obtained and modified from SmartHome and CompositeDrawingEditorComplete
// https://github.com/UBCx-Software-Construction/long-form-problem-starters.git
// https://github.students.cs.ubc.ca/CPSC210/CompositeDrawingEditorComplete.git
// the Aircraft tab
public class AircraftTab extends Tab {

    private JTextArea textArea;

    // MODIFIES: this
    // EFFECTS: creates an aircraft tab
    public AircraftTab(GUI controller) {
        super(controller);
    }

    @Override
    protected void placeToolButtons() {
        NewAircraftTool newAircraftTool = new NewAircraftTool(this);
        tools.put("new aircraft", newAircraftTool);
        newAircraftTool.activate();
        toolArea.add(newAircraftTool.getButton());

        EditFuelTool editFuelTool = new EditFuelTool(this);
        tools.put("edit fuel", editFuelTool);
        toolArea.add(editFuelTool.getButton());
    }

    @Override
    protected void initInfo() {
        infoArea = new JPanel();
        infoArea.setLayout(new GridLayout());
        infoArea.setVisible(true);

        textArea = new JTextArea();
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        textArea.setEditable(false);

        infoArea.add(textArea);
        add(infoArea, infoConstraints());
    }

    @Override
    protected void placeInfo() {
        Aircraft aircraft = getController().getAircraft();
        if (aircraft == null) {
            tools.get("edit fuel").deactivate();
            textArea.setText("No loaded aircraft");
        } else {
            tools.get("edit fuel").activate();
            textArea.setText("Registration: " + aircraft.getRegistration() + NLINE
                    + "Fuel: " + aircraft.getFuel() + NLINE
                    + "Fuel Burn: " + aircraft.getFuelBurn() + "/hr" + NLINE
                    + "Cruise Speed: " + aircraft.getCruiseSpeed() + "kts");
        }
    }
}
