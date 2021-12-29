package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlightPlanTest {
    private FlightPlan fp1;
    private FlightPlan fp2;

    private Map m1;
    private Map m2;

    private Checkpoint c2;
    private Checkpoint c4;
    private Checkpoint c5;
    private Airport a1;
    private Airport a2;
    private Airport a3;

    private Aircraft ac1;
    private Aircraft ac2;

    @BeforeEach
    public void setUp() {
        Leg l1;
        Leg l2r;
        Leg l3r;
        Leg l4r;
        Leg l5;

        List<Runway> runways;
        Runway r1;
        Runway r2;

        runways = new ArrayList<>();
        r1 = new Runway(26);
        r2 = new Runway(6);
        runways.add(r1);
        runways.add(r2);

        c2 = new Checkpoint("Keefers", new ArrayList<>());
        a3 = new Airport("Chilliwack", new ArrayList<>(), "CYCW", 1500, runways);
        c4 = new Checkpoint("Fort Langley", new ArrayList<>());

        a1 = new Airport("Hope", new ArrayList<>(), "CYHE");

        l2r = new Leg(a1, 175, 38);
        l3r = new Leg(a1, 190, 24);
        l4r = new Leg(a1, 74, 44);
        c2.addLeg(l2r);
        a3.addLeg(l3r);
        c4.addLeg(l4r);

        l5 = new Leg(a3, 92, 24);
        c4.addLeg(l5);

        m1 = new Map("Mountains", -16);
        m1.addAirport(a1);
        m1.addCheckpoint(c2);
        m1.addAirport(a3);
        m1.addCheckpoint(c4);

        List<Leg> legs = new ArrayList<>();
        a2 = new Airport("Pitt Meadows", new ArrayList<>(), "CYPK", 1500, runways);
        l1 = new Leg(a2, 139, 7);
        legs.add(l1);
        c5 = new Checkpoint("Port Moody", legs);

        m2 = new Map("Burrard Inlet", 50);
        m2.addAirport(a2);
        m2.addCheckpoint(c5);

        ac1 = new Aircraft("GMSD", 24.5, 5.4, 90);
        ac2 = new Aircraft("GMUF", 40, 6.7, 114);

        fp1 = new FlightPlan(ac1, m1, "CYHE", "CYCW");
        fp2 = new FlightPlan(ac2, m2, "CYPK", "CYPK");
    }

    @Test
    public void testInit() {
        assertEquals(fp1.getMap(), m1);
        assertEquals(fp1.getDeparture(), a1);
        assertEquals(fp1.getDestination(), a3);
        assertEquals(fp1.getEnRoutePoints().size(), 0);

        assertEquals(fp2.getMap(), m2);
        assertEquals(fp2.getDeparture(), a2);
        assertEquals(fp2.getDestination(), a2);
        assertEquals(fp2.getEnRoutePoints().size(), 0);
    }

    @Test
    public void testSetters() {
        Aircraft aa = new Aircraft("GMUF", 40, 6.8, 115);
        assertEquals(fp1.getAircraft().getRegistration() , "GMSD");
        fp1.setAircraft(aa);
        assertEquals(fp1.getAircraft().getRegistration(), "GMUF");
    }

    @Test
    public void testCalculateRoute() {
        assertEquals(fp1.getCpNames().size(), 2);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Chilliwack");

        assertEquals(fp2.getCpNames().size(), 1);
        assertEquals(fp2.getCpNames().get(0), "Pitt Meadows");
    }

    @Test
    public void testAddEnRoutePointBack() {
        fp2.addEnRoutePoint("Port Moody");
        assertEquals(fp2.getCpNames().size(), 3);
        assertEquals(fp2.getCpNames().get(0), "Pitt Meadows");
        assertEquals(fp2.getCpNames().get(1), "Port Moody");
        assertEquals(fp2.getCpNames().get(2), "Pitt Meadows");

        assertEquals(fp2.getEnRoutePoints().size(), 1);
        assertEquals(fp2.getEnRoutePoints().get(0), "Port Moody");
    }

    @Test
    public void testAddEnRoutePointAround() {
        fp1.addEnRoutePoint("Fort Langley");
        assertEquals(fp1.getCpNames().size(), 3);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Fort Langley");
        assertEquals(fp1.getCpNames().get(2), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 1);
        assertEquals(fp1.getEnRoutePoints().get(0), "Fort Langley");
    }

    @Test
    public void testAddEnRoutePointDoubleBack() {
        fp1.addEnRoutePoint("Keefers");
        assertEquals(fp1.getCpNames().size(), 4);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Keefers");
        assertEquals(fp1.getCpNames().get(2), "Hope");
        assertEquals(fp1.getCpNames().get(3), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 1);
        assertEquals(fp1.getEnRoutePoints().get(0), "Keefers");
    }

    @Test
    public void testAddEnRoutePointDestination() {
        fp1.addEnRoutePoint("Chilliwack");
        assertEquals(fp1.getCpNames().size(), 2);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 1);
        assertEquals(fp1.getEnRoutePoints().get(0), "Chilliwack");
    }

    @Test
    public void testAddEnRoutePointUnreachable() {
        fp1.addEnRoutePoint("Chilliwack");
        try {
            fp1.addEnRoutePoint("Sechelt");
            fail("null pointer exception expected");
        } catch (NullPointerException e) {
            // expected
        }
        assertEquals(fp1.getCpNames().size(), 2);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 1);
        assertEquals(fp1.getEnRoutePoints().get(0), "Chilliwack");
    }

    @Test
    public void testAddEnRoutePointTwo() {
        fp1.addEnRoutePoint("Fort Langley");
        fp1.addEnRoutePoint("Keefers");
        assertEquals(fp1.getCpNames().size(), 6);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Fort Langley");
        assertEquals(fp1.getCpNames().get(2), "Hope");
        assertEquals(fp1.getCpNames().get(3), "Keefers");
        assertEquals(fp1.getCpNames().get(4), "Hope");
        assertEquals(fp1.getCpNames().get(5), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 2);
        assertEquals(fp1.getEnRoutePoints().get(0), "Fort Langley");
        assertEquals(fp1.getEnRoutePoints().get(1), "Keefers");
    }

    @Test
    public void testAddEnRoutePointFour() {
        fp1.addEnRoutePoint("Chilliwack");
        fp1.addEnRoutePoint("Fort Langley");
        fp1.addEnRoutePoint("Keefers");
        fp1.addEnRoutePoint("Fort Langley");
        assertEquals(fp1.getCpNames().size(), 8);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Chilliwack");
        assertEquals(fp1.getCpNames().get(2), "Fort Langley");
        assertEquals(fp1.getCpNames().get(3), "Hope");
        assertEquals(fp1.getCpNames().get(4), "Keefers");
        assertEquals(fp1.getCpNames().get(5), "Hope");
        assertEquals(fp1.getCpNames().get(6), "Fort Langley");
        assertEquals(fp1.getCpNames().get(7), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 4);
        assertEquals(fp1.getEnRoutePoints().get(0), "Chilliwack");
        assertEquals(fp1.getEnRoutePoints().get(1), "Fort Langley");
        assertEquals(fp1.getEnRoutePoints().get(2), "Keefers");
        assertEquals(fp1.getEnRoutePoints().get(3), "Fort Langley");
    }

    @Test
    public void testAddEnRoutePointManyDuplicate() {
        fp1.addEnRoutePoint("Hope");
        fp1.addEnRoutePoint("Fort Langley");
        fp1.addEnRoutePoint("Fort Langley");
        fp1.addEnRoutePoint("Keefers");
        assertEquals(fp1.getCpNames().size(), 6);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Fort Langley");
        assertEquals(fp1.getCpNames().get(2), "Hope");
        assertEquals(fp1.getCpNames().get(3), "Keefers");
        assertEquals(fp1.getCpNames().get(4), "Hope");
        assertEquals(fp1.getCpNames().get(5), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 3);
        assertEquals(fp1.getEnRoutePoints().get(0), "Hope");
        assertEquals(fp1.getEnRoutePoints().get(1), "Fort Langley");
        assertEquals(fp1.getEnRoutePoints().get(2), "Keefers");
    }

    @Test
    public void testAddEnRoutePointManyOver() {
        fp1.addEnRoutePoint("Chilliwack");
        fp1.addEnRoutePoint("Fort Langley");
        assertEquals(fp1.getCpNames().size(), 4);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Chilliwack");
        assertEquals(fp1.getCpNames().get(2), "Fort Langley");
        assertEquals(fp1.getCpNames().get(3), "Chilliwack");

        assertEquals(fp1.getEnRoutePoints().size(), 2);
        assertEquals(fp1.getEnRoutePoints().get(0), "Chilliwack");
        assertEquals(fp1.getEnRoutePoints().get(1), "Fort Langley");
    }


    @Test
    public void testRemoveEnRoutePoints() {
        fp1.addEnRoutePoint("Chilliwack");
        fp1.addEnRoutePoint("Fort Langley");
        fp1.removeEnRoutePoints();

        assertEquals(fp1.getCpNames().size(), 2);
        assertEquals(fp1.getCpNames().get(0), "Hope");
        assertEquals(fp1.getCpNames().get(1), "Chilliwack");
        assertEquals(fp1.getEnRoutePoints().size(), 0);
    }

    @Test
    public void testGetHeadings() {
        assertEquals(fp1.getHeadings().size(), 1);
        assertEquals(fp1.getHeadings().get(0), 10);

        assertEquals(fp2.getHeadings().size(), 0);
        fp2.addEnRoutePoint("Port Moody");
        assertEquals(fp2.getHeadings().size(), 2);
        assertEquals(fp2.getHeadings().get(0), 319);
        assertEquals(fp2.getHeadings().get(1), 139);
    }

    @Test
    public void testGetDistances() {
        assertEquals(fp2.getDistances().size(), 0);
        assertEquals(fp1.getDistances().size(), 1);
        assertEquals(fp1.getDistances().get(0), 24);
        fp1.addEnRoutePoint("Fort Langley");
        assertEquals(fp1.getDistances().size(), 2);
        assertEquals(fp1.getDistances().get(0), 44);
        assertEquals(fp1.getDistances().get(1), 24);
    }

    @Test
    public void testCalculateHeadingsOne() {
        assertEquals(fp1.calculateHeadings().size(), 1);
        assertEquals(fp1.calculateHeadings().get(0), 354);
        assertEquals(fp2.calculateHeadings().size(), 0);
    }

    @Test
    public void testCalculateHeadingsMany() {
        fp1.addEnRoutePoint("Fort Langley");
        assertEquals(fp1.calculateHeadings().size(), 2);
        assertEquals(fp1.calculateHeadings().get(0), 254 - 16);
        assertEquals(fp1.calculateHeadings().get(1), 92 - 16);

        fp2.addEnRoutePoint("Port Moody");
        assertEquals(fp2.calculateHeadings().size(), 2);
        assertEquals(fp2.calculateHeadings().get(0), 9);
        assertEquals(fp2.calculateHeadings().get(1), 189);
    }

    @Test
    public void testCalculateETEsOne() {
        assertEquals(fp1.calculateETEs().size(), 1);
        assertEquals(fp1.calculateETEs().get(0), (double) 24 * 60 / 90);
        assertEquals(fp2.calculateETEs().size(), 0);
    }

    @Test
    public void testCalculateETEsMany() {
        fp1.addEnRoutePoint("Fort Langley");
        assertEquals(fp1.calculateETEs().size(), 2);
        assertEquals(fp1.calculateETEs().get(0), Math.round(44.0 * 600.0 / 90.0) / 10.0);
        assertEquals(fp1.calculateETEs().get(1), Math.round(24.0 * 600.0 / 90.0) / 10.0);
    }

    @Test
    public void testCalculateFuelBurns() {
        assertEquals(fp2.calculateFuelBurns().size(), 0);

        assertEquals(fp1.calculateFuelBurns().size(), 1);
        assertEquals(fp1.calculateFuelBurns().get(0), 1.5);
        fp1.addEnRoutePoint("Fort Langley");
        assertEquals(fp1.calculateFuelBurns().size(), 2);
        assertEquals(fp1.calculateFuelBurns().get(0), 2.7);
        assertEquals(fp1.calculateFuelBurns().get(1), 1.5);
    }
}
