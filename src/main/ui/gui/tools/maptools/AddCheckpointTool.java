package ui.gui.tools.maptools;

import model.Checkpoint;
import ui.gui.PopupWindow;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// tool to add checkpoint to map
public class AddCheckpointTool extends Tool {

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates an add checkpoint tool
    public AddCheckpointTool(Tab controller) {
        super(controller, "Add Checkpoint");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = PopupWindow.getNonEmptyString("enter the checkpoint's name");
                if (mainController().getMap().getCheckpoint(name) != null) {
                    PopupWindow.showMessage("This name is already taken");
                    return;
                }
                Checkpoint checkpoint = new Checkpoint(name, new ArrayList<>());
                mainController().getMap().addCheckpoint(checkpoint);
                mainController().update();
            }
        });
    }
}
