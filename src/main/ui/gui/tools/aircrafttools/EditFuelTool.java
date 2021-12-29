package ui.gui.tools.aircrafttools;

import ui.gui.PopupWindow;
import ui.gui.SoundPlayer;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// tool to edit aircraft fuel
public class EditFuelTool extends Tool {

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates an edit fuel tool
    public EditFuelTool(Tab controller) {
        super(controller, "Edit Fuel");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double fuel = PopupWindow.getDouble("Enter new fuel quantity", 0);
                if (mainController().getAircraft().getFuel() < fuel) {
                    SoundPlayer.playSound("refueling_noises.wav");
                }
                mainController().getAircraft().setFuel(fuel);
                mainController().update();
            }
        });
    }
}
