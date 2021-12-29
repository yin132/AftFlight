package ui.gui.tools.flightplantools;

import model.Airport;
import model.FlightPlan;
import ui.gui.PopupWindow;
import ui.gui.SoundPlayer;
import ui.gui.tabs.FlightPlanTab;
import ui.gui.tabs.Tab;
import ui.gui.tools.Tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// a tool to make a new flight plan
public class NewFlightPlanTool extends Tool {

    // MODIFIES: this
    // EFFECTS: makes new flight plan tool
    public NewFlightPlanTool(Tab controller) {
        super(controller, "New Flight Plan");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FlightPlanTab subC = (FlightPlanTab) subController;
                if (!subC.isFlightplanActive()) {
                    Airport departure = subC.getSelectedDeparture();
                    Airport destination = subC.getSelectedDestination();

                    if (departure == null || destination == null) {
                        PopupWindow.showMessage("Select one or two airports to file a flight plan");
                        return;
                    }
                    FlightPlan flightPlan = tryMakeFlightplan(departure, destination);
                    if (flightPlan == null) {
                        return;
                    }
                    SoundPlayer.playSound("airplane_noises.wav");
                    mainController().setFlightPlan(flightPlan);
                } else {
                    mainController().setFlightPlan(null);
                }
                mainController().update();
            }
        });
    }

    private FlightPlan tryMakeFlightplan(Airport departure, Airport destination) {
        FlightPlan flightPlan;
        try {
            flightPlan = new FlightPlan(mainController().getAircraft(),
                    mainController().getMap(), departure.getCode(), destination.getCode());
        } catch (NullPointerException exception) {
            PopupWindow.showMessage("Could not find path between airports");
            return null;
        }
        return flightPlan;
    }
}
