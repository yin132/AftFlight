package ui.gui.tools.maptools;

import model.Checkpoint;
import model.Leg;
import ui.gui.PopupWindow;
import ui.gui.tabs.MapTab;
import ui.gui.tabs.SelectUtil;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// tool to add leg to map
public class AddLegTool extends Tool {

    Checkpoint departurePoint;

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates an add leg tool
    public AddLegTool(Tab controller) {
        super(controller, "Add Leg");
        departurePoint = null;
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SelectUtil selectUtil = ((MapTab) (subController)).getSelectTool();
                if (departurePoint == null) {
                    startAddLeg();
                } else {
                    Checkpoint arrivalPoint = completeAddLeg();
                    if (arrivalPoint == null) {
                        return;
                    }
                    selectUtil.updateLabel("");
                    selectUtil.labelVisibility(false);
                    mainController().update();
                    departurePoint = null;
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: begins process of adding leg
    private void startAddLeg() {
        SelectUtil selectUtil = ((MapTab) (subController)).getSelectTool();
        departurePoint = selectUtil.getOneCheckpoint();
        if (departurePoint == null) {
            return;
        }
        selectUtil.updateLabel("Select a different airport or checkpoint"
                + "as destination and press add leg");
        selectUtil.labelVisibility(true);
    }

    // MODIFIES: this
    // EFFECTS: completes adding a leg
    private Checkpoint completeAddLeg() {
        SelectUtil selectUtil = ((MapTab) (subController)).getSelectTool();
        Checkpoint arrivalPoint = selectUtil.getOneCheckpoint();
        if (arrivalPoint == null || arrivalPoint == departurePoint) {
            return null;
        }
        int heading = PopupWindow.getInt("heading to get there", 1, 360);
        int distance = PopupWindow.getInt("distance to get there", 0);
        mainController().getMap().addLegTo(departurePoint.getName(),
                new Leg(arrivalPoint, heading, distance));
        return arrivalPoint;
    }
}
