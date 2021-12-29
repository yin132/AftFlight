package ui.gui.menus;

import ui.gui.GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

// Code obtained and modified from components-MenuDemoProject
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
// an abstract dropdown menu
public abstract class Menu extends JMenu implements ActionListener {
    protected MenuBar subController;

    // REQUIRES: controller is menu bar this belongs in
    // MODIFIES: this
    // EFFECTS: creates a menu
    public Menu(MenuBar controller, String label) {
        super(label);
        this.subController = controller;
    }

    // EFFECTS: returns the main GUI controller
    protected GUI mainController() {
        return subController.getController();
    }

    // MODIFIES: this
    // EFFECTS: initializes a menu item
    protected void initMenuItem(String label) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.addActionListener(this);
        add(menuItem);
    }

    // MODIFIES: this
    // EFFECTS: toggles visibility of menu items
    public abstract void update();
}
