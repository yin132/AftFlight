package ui.gui;

import model.Aircraft;
import model.FlightPlan;
import model.Map;
import ui.gui.menus.MenuBar;
import ui.gui.menus.Menu;
import ui.gui.tabs.*;

import javax.swing.*;
import java.awt.*;

// Code obtained and modified from SmartHome
// https://github.com/UBCx-Software-Construction/long-form-problem-starters.git
// runs the AftFlight gui
public class GUI extends JFrame {

    public static final int MAP_TAB_INDEX = 0;
    public static final int AIRCRAFT_TAB_INDEX = 1;
    public static final int FLIGHTPLAN_TAB_INDEX = 2;

    public static final int WIDTH = 1500;
    public static final int HEIGHT = 1000;
    public static final String NLINE = "\n";
    private JTabbedPane sidebar;
    private JMenuBar menuBar;

    private Map map;
    private Aircraft aircraft;
    private FlightPlan flightPlan;

    public static void main(String[] args) {
        new GUI();
    }

    //MODIFIES: this
    //EFFECTS: displays and runs dropdown tabs, sidebar, and page tabs
    private GUI() {
        super("AftFlight");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initData();
        initSideBar();
        initMenus();
        update();

        setVisible(true);
    }

    // getters

    public Map getMap() {
        return map;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public FlightPlan getFlightPlan() {
        return flightPlan;
    }

    // setters

    public void setMap(Map map) {
        this.map = map;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public void setFlightPlan(FlightPlan flightPlan) {
        this.flightPlan = flightPlan;
    }

    // MODIFIES: this
    // EFFECTS: initializes menus
    private void initMenus() {
        menuBar = new MenuBar(this);
        setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS: initializes tabs side bar
    private void initSideBar() {
        sidebar = new JTabbedPane();
        sidebar.setTabPlacement(JTabbedPane.LEFT);
        loadTabs();
        add(sidebar);
    }

    // MODIFIES: this
    // EFFECTS: initializes data
    private void initData() {
        map = new Map("Lower Mainland", -16);
    }

    // MODIFIES: this
    // EFFECTS: adds map tab, aircraft tab and flightplan tab to this UI
    private void loadTabs() {
        JPanel mapTab = new MapTab(this);
        JPanel aircraftTab = new AircraftTab(this);
        JPanel flightPlanTab = new FlightPlanTab(this);

        sidebar.add(mapTab, MAP_TAB_INDEX);
        sidebar.setTitleAt(MAP_TAB_INDEX, "Map");
        sidebar.add(aircraftTab, AIRCRAFT_TAB_INDEX);
        sidebar.setTitleAt(AIRCRAFT_TAB_INDEX, "Aircraft");
        sidebar.add(flightPlanTab, FLIGHTPLAN_TAB_INDEX);
        sidebar.setTitleAt(FLIGHTPLAN_TAB_INDEX, "Flight Plan");
    }

    // MODIFIES: this
    // EFFECTS: updates all tabs
    public void update() {
        for (int k = 0; k < sidebar.getComponentCount(); k++) {
            ((Tab) sidebar.getComponent(k)).update();
        }

        for (int k = 0; k < menuBar.getComponentCount(); k++) {
            ((Menu) menuBar.getComponent(k)).update();
        }
    }

}
