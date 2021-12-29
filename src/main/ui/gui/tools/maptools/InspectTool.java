package ui.gui.tools.maptools;

import model.Airport;
import model.Checkpoint;
import model.Leg;
import model.Runway;
import ui.gui.PopupWindow;
import ui.gui.tabs.MapTab;
import ui.gui.tabs.SelectUtil;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.gui.GUI.NLINE;

// tool to inspect details of a checkpoint
public class InspectTool extends Tool {

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates an inspect tool
    public InspectTool(Tab controller) {
        super(controller, "Inspect");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SelectUtil selectUtil = ((MapTab) subController).getSelectTool();
                Checkpoint checkpoint = selectUtil.getOneCheckpoint();
                if (checkpoint == null) {
                    return;
                }
                String message = getCheckpointInfo(checkpoint);
                PopupWindow.showMessage(message);
            }
        });
    }

    // REQUIRES: checkpoint is not null
    // EFFECTS: changes checkpoint to its information as a string
    private String getCheckpointInfo(Checkpoint checkpoint) {
        String title;
        String legsInfo = "";
        for (Leg l : checkpoint.getLegs()) {
            legsInfo += "to: " + l.getDestName() + ", heading: " + l.getHeading()
                    + ", distance: " + l.getDistance() + NLINE;
        }
        String message;

        if (!(checkpoint instanceof Airport)) {
            title = "Checkpoint " + checkpoint.getName() + ":" + NLINE;
            message = title + NLINE + legsInfo;
        } else {
            Airport airport = (Airport) checkpoint;
            title = "Airport " + airport.getName() + " " + airport.getCode() + NLINE;
            String airportInfo = "arrival altitude: " + airport.getArrivalAlt() + "ft" + NLINE;
            String runwayInfo = "";
            for (Runway r : airport.getRunways()) {
                runwayInfo += "runway: " + r.getName() + " " + r.getCondition() + NLINE;
            }
            message = title + NLINE + legsInfo + NLINE + airportInfo + NLINE + runwayInfo;
        }
        return message;
    }
}
