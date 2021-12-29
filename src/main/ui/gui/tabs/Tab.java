package ui.gui.tabs;

import ui.gui.GUI;
import ui.gui.tools.Tool;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static java.awt.GridBagConstraints.*;
import static ui.gui.tools.Tool.TOOL_WIDTH;

// Code obtained and modified from SmartHome and CompositeDrawingEditorComplete
// https://github.com/UBCx-Software-Construction/long-form-problem-starters.git
// https://github.students.cs.ubc.ca/CPSC210/CompositeDrawingEditorComplete.git
// a tab
public abstract class Tab extends JPanel {
    protected Map<String, Tool> tools;
    protected GUI controller;
    protected JPanel toolArea;
    protected JPanel infoArea;

    // REQUIRES: GUI controller that holds this tab
    // MODIFIES: this
    // EFFECTS: creates a tab for controller
    public Tab(GUI controller) {
        this.tools = new HashMap<>();
        this.controller = controller;
        setLayout(new GridBagLayout());
        initToolDisplay();
        placeToolButtons();
        initInfo();
        placeInfo();
    }

    // getters

    public GUI getController() {
        return controller;
    }

    // MODIFIES: this
    // EFFECTS: initializes the tools display
    protected void initToolDisplay() {
        toolArea = new JPanel();
        toolArea.setLayout(new GridLayout(0, 1));
        toolArea.setSize(new Dimension(TOOL_WIDTH, HEIGHT));

        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridx = 0;
//        constraints.gridy = 0;
        constraints.weightx = 0.2;
        constraints.ipady = GUI.HEIGHT - 200;
        constraints.anchor = FIRST_LINE_START;

        add(toolArea, constraints);
    }

    // EFFECTS: returns the constraints for the info display
    protected GridBagConstraints infoConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridx = 1;
//        constraints.gridy = 0;
        constraints.anchor = FIRST_LINE_END;
        constraints.weightx = 1;
        constraints.ipadx = 400;
        constraints.fill = BOTH;
        return constraints;
    }

    // MODIFIES: this
    // EFFECTS: updates the tab
    public void update() {
        placeInfo();
    }

    // MODIFIES: this
    // EFFECTS: places the tools for the tab
    protected abstract void placeToolButtons();

    // MODIFIES: this
    // EFFECTS: initializes the info display
    protected abstract void initInfo();

    // MODIFIES: this
    // EFFECTS: displays the info for the tab
    protected abstract void placeInfo();
}
