package ui.gui.tabs;

import ui.exceptions.NoSelectionException;
import ui.exceptions.TooManySelectedException;
import model.Airport;
import model.Checkpoint;
import ui.gui.GUI;
import ui.gui.PopupWindow;

import javax.swing.*;

public class SelectUtil {

    protected Tab subController;
    private Checkpoint checkpoint;
    private Airport airport;
    private JLabel label;

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates a select utility
    public SelectUtil(Tab controller, JLabel label) {
        this.subController = controller;
        this.label = label;
    }

    // setters

    public void setLabel(JLabel label) {
        this.label = label;
    }

    // MODIFIES: this
    // EFFECTS: sets label to visible if true, else sets to not visible
    public void labelVisibility(boolean visible) {
        label.setVisible(visible);
    }

    // MODIFIES: this
    // EFFECTS: changes the text on the label to text
    public void updateLabel(String text) {
        label.setText(text);
    }

    // MODIFIES: this
    // EFFECTS: returns a selected checkpoint, throws exception if multiple are selected
    public Checkpoint getSelectedCheckpoint() throws TooManySelectedException, NoSelectionException {
        readCheckpoint();
        readAirport();
        if (airport != null && checkpoint != null) {
            throw new TooManySelectedException();
        } else if (airport != null) {
            return airport;
        } else if (checkpoint != null) {
            return checkpoint;
        } else {
            throw new NoSelectionException();
        }
    }

    // MODIFIES: this
    // EFFECTS: returns the selected checkpoint
    public Checkpoint getOneCheckpoint() {
        try {
            Checkpoint checkpoint = getSelectedCheckpoint();
            return checkpoint;
        } catch (TooManySelectedException tooManySelectedException) {
            PopupWindow.showMessage("please only select one checkpoint or airport");
        } catch (NoSelectionException noSelectionException) {
            PopupWindow.showMessage("please select one checkpoint or airport");
        }
        return null;
    }

//    // MODIFIES: this
//    // EFFECTS: returns the selected airport, throws exception if none selected
//    public Airport getSelectedAirport() throws NoSelectionException {
//        readAirport();
//        if (airport == null) {
//            throw new NoSelectionException();
//        }
//        return airport;
//    }

    protected GUI mainController() {
        return subController.getController();
    }

    // MODIFIES: this
    // EFFECTS: reads the checkpoint from controller into checkpoint
    private void readCheckpoint() {
        String checkpointName = ((MapTab) subController).getSelectedCheckpoint();
        if (checkpointName == null) {
            checkpoint = null;
        } else {
            this.checkpoint = mainController().getMap().getCheckpoint(checkpointName);
        }
    }

    // MODIFIES: this
    // EFFECTS: reads the airport from controller into checkpoint
    private void readAirport() {
        String airportName = ((MapTab) subController).getSelectedAirport();
        if (airportName == null) {
            airport = null;
        } else {
            this.airport = (Airport) mainController().getMap().getCheckpoint(airportName);
        }
    }
}
