package ui.gui.tools.flightplantools;

import model.Checkpoint;
import ui.gui.PopupWindow;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// a tool to add en-route points
public class AddEnRoutePointTool extends Tool {

    // MODIFIES: this
    // EFFECTS: makes add en-route point tool
    public AddEnRoutePointTool(Tab controller) {
        super(controller, "Add En-Route Point");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Checkpoint> checkpoints = mainController().getMap().getCheckpoints();
                List<String> checkpointNames = new ArrayList<>();
                for (Checkpoint checkpoint : checkpoints) {
                    checkpointNames.add(checkpoint.getName());
                }
                try {
                    String name = PopupWindow.getString("Pick a checkpoint to overfly", checkpointNames);
                    mainController().getFlightPlan().addEnRoutePoint(name);
                } catch (NullPointerException exception) {
                    PopupWindow.showMessage("Cannot reach that checkpoint");
                }
                mainController().update();
            }
        });
    }
}
