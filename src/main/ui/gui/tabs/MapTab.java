package ui.gui.tabs;

import model.Airport;
import model.Checkpoint;
import ui.gui.GUI;
import ui.gui.tools.maptools.*;

import javax.swing.*;
import java.awt.*;

// Code obtained and modified from SmartHome and CompositeDrawingEditorComplete
// https://github.com/UBCx-Software-Construction/long-form-problem-starters.git
// https://github.students.cs.ubc.ca/CPSC210/CompositeDrawingEditorComplete.git
// the Map tab
public class MapTab extends Tab {

    JList checkpointList;
    JList airportList;
    SelectUtil selectUtil;

    // MODIFIES: this
    // EFFECTS: makes a map tab
    public MapTab(GUI controller) {
        super(controller);
    }

    // getters

    public SelectUtil getSelectTool() {
        return selectUtil;
    }

    // EFFECTS: returns the selected checkpoint
    public String getSelectedCheckpoint() {
        return (String) checkpointList.getSelectedValue();
    }

    // EFFECTS: returns the selected airport
    public String getSelectedAirport() {
        return (String) airportList.getSelectedValue();
    }

    @Override
    protected void placeToolButtons() {
        AddCheckpointTool addCheckpointTool = new AddCheckpointTool(this);
        tools.put("add checkpoint", addCheckpointTool);
        addCheckpointTool.activate();
        toolArea.add(addCheckpointTool.getButton());

        AddAirportTool addAirportTool = new AddAirportTool(this);
        tools.put("add airport", addAirportTool);
        addAirportTool.activate();
        toolArea.add(addAirportTool.getButton());

        AddLegTool addLegTool = new AddLegTool(this);
        tools.put("add leg", addLegTool);
        toolArea.add(addLegTool.getButton());

        InspectTool inspectTool = new InspectTool(this);
        tools.put("inspect", inspectTool);
        toolArea.add(inspectTool.getButton());
    }

    @Override
    protected void initInfo() {
        infoArea = new JPanel();
        infoArea.setLayout(new GridLayout(3, 0));
        infoArea.setVisible(true);

        initControlArea();

        checkpointList = new JList();
        airportList = new JList();

        checkpointList.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        airportList.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        checkpointList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        airportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        checkpointList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        airportList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        JScrollPane checkpointScroll = new JScrollPane(checkpointList);
        JScrollPane airportScroll = new JScrollPane(airportList);

        infoArea.add(checkpointScroll, 0);
        infoArea.add(airportScroll, 1);

        add(infoArea, infoConstraints());
    }

    @Override
    protected void placeInfo() {
        checkpointList.removeAll();
        airportList.removeAll();

        DefaultListModel<String> checkpointNames = new DefaultListModel<>();
        DefaultListModel<String> airportNames = new DefaultListModel<>();

        String note = "(select this to deselect)";
        checkpointNames.addElement("Checkpoints " + note + ":");
        airportNames.addElement("Airports " + note + ":");
        for (Checkpoint checkpoint : controller.getMap().getCheckpoints()) {
            if (!(checkpoint instanceof Airport)) {
                checkpointNames.addElement(checkpoint.getName());
            } else {
                airportNames.addElement(checkpoint.getName());
            }
        }

        checkpointList.setModel(checkpointNames);
        airportList.setModel(airportNames);
    }

    @Override
    public void update() {
        super.update();
        selectUtil.labelVisibility(false);
        if (canAddLeg()) {
            tools.get("add leg").activate();
        } else {
            tools.get("add leg").deactivate();
        }
        if (canInspect()) {
            tools.get("inspect").activate();
        } else {
            tools.get("inspect").deactivate();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the control area for the select tool
    private void initControlArea() {
        JPanel controlArea = new JPanel();
        controlArea.setLayout(new GridLayout(2, 1));
        controlArea.setVisible(true);

        JLabel label = new JLabel();
        selectUtil = new SelectUtil(this, label);
        selectUtil.setLabel(label);
        controlArea.add(label);

        infoArea.add(controlArea);
    }

    // EFFECTS: returns true if there are two or more checkpoints on map
    private boolean canAddLeg() {
        return controller.getMap().getCheckpoints().size() >= 2;
    }

    // EFFECTS: returns true if there is at least one checkpoint on map
    private boolean canInspect() {
        return controller.getMap().getCheckpoints().size() >= 1;
    }

}
