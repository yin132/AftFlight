package ui.gui.menus;

import ui.gui.GUI;

import javax.swing.*;

// Code obtained and modified from components-MenuDemoProject
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
// creates the menu bar for dropdown menus
public class MenuBar extends JMenuBar {
    private GUI controller;

    // REQUIRES: controller is the gui this belongs in
    // MODIFIES: this
    // EFFECTS: creates the menu bar with menus
    public MenuBar(GUI controller) {
        super();
        this.controller = controller;

        initSaveMenu();
        initLoadMenu();
    }

    // getters

    public GUI getController() {
        return controller;
    }

    // MODIFIES: this
    // EFFECTS: makes and adds the load menu
    private void initLoadMenu() {
        JMenu menu = new LoadMenu(this);
        add(menu);
    }

    // MODIFIES: this
    // EFFECTS: makes and adds the save menu
    private void initSaveMenu() {
        JMenu menu = new SaveMenu(this);
        add(menu);
    }
}
