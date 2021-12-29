package ui.gui.tools;

import ui.gui.GUI;
import ui.gui.tabs.Tab;

import javax.swing.*;

// Code obtained and modified from CompositeDrawingEditorComplete
// https://github.students.cs.ubc.ca/CPSC210/CompositeDrawingEditorComplete.git
// a tool for the gui
public abstract class Tool {
    public static final int TOOL_WIDTH = 120;

    // the tool's associated button
    protected JButton button;
    protected boolean active;
    protected Tab subController;

    // REQUIRES: controller is the tab this belongs to
    // MODIFIES: this
    // EFFECTS: creates a tool
    public Tool(Tab controller, String label) {
        this.subController = controller;
        createButton(label);
        deactivate();
        addListener();
    }

    // getters

    public JButton getButton() {
        return button;
    }

//    // EFFECTS: returns true if tool is active
//    protected boolean isActive() {
//        return active;
//    }

    // MODIFIES: this
    // EFFECTS: sets tool to active
    public void activate() {
        active = true;
        button.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets tool to not active
    public void deactivate() {
        active = false;
        button.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: creates the tool's button
    private void createButton(String label) {
        button = new JButton(label);
    }


    // EFFECTS: returns the main gui controller
    public GUI mainController() {
        return subController.getController();
    }

    // MODIFIES: controller in subController
    // EFFECTS
    protected abstract void addListener();
}
