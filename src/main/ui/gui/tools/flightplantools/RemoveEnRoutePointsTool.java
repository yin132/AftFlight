package ui.gui.tools.flightplantools;

import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// a tool to remove all en-route points
public class RemoveEnRoutePointsTool extends Tool {

    // MODIFIES: this
    // EFFECTS: makes a remove en-route points tool
    public RemoveEnRoutePointsTool(Tab controller) {
        super(controller, "Remove En-Route Points");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainController().getFlightPlan().removeEnRoutePoints();
                mainController().update();
            }
        });
    }
}
