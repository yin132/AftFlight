package persistence;

import model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    protected Map m1;

    protected Checkpoint c2;
    protected Checkpoint c4;
    protected Checkpoint c5;
    protected Checkpoint c7;
    protected Airport a1;
    protected Airport a2;
    protected Airport a3;
    protected Airport a6;

    protected Leg l1;
    protected Leg l2r;
    protected Leg l3r;
    protected Leg l4r;
    protected Leg l5;
    protected Leg l6;
    protected Leg l7;
    protected Leg l8;

    protected List<Runway> runways;
    protected Runway r1;
    protected Runway r2;

    protected Aircraft ac;

    protected void setUpMap() {
        runways = new ArrayList<>();
        r1 = new Runway(18);
        r2 = new Runway(26, 'R', 2400, 75, 'H');
        r2.setNotes("slightly sloped");
        runways.add(r1);
        runways.add(r2);

        c2 = new Checkpoint("Keefers", new ArrayList<>());
        c4 = new Checkpoint("Fort Langley", new ArrayList<>());
        c7 = new Checkpoint("Greenhouse", new ArrayList<>());
        c5 = new Checkpoint("Port Moody", new ArrayList<>());
        a1 = new Airport("Hope", new ArrayList<>(), "CYHE");
        a2 = new Airport("Pitt Meadows", new ArrayList<>(), "CYPK", 1500, runways);
        a3 = new Airport("Chilliwack", new ArrayList<>(), "CYCW", 1500);
        a6 = new Airport("Abbotsford", new ArrayList<>(), "CYXX");

        l1 = new Leg(a2, 46, 7);
        l2r = new Leg(a1, 175, 38);
        l3r = new Leg(a1, 55, 24);
        l4r = new Leg(c4, 321, 12);
        l7 = new Leg(c7, 240, 10);
        l8 = new Leg(a6, 252, 9);
        l5 = new Leg(a3, 92, 24);
        l6 = new Leg(c4, 117, 7);

        a2.addLeg(l6);
        c2.addLeg(l2r);
        c7.addLeg(l8);
        a3.addLeg(l3r);
        a3.addLeg(l7);
        a6.addLeg(l4r);
        c4.addLeg(l5);
        c5.addLeg(l1);

        m1 = new Map("Lower Mainland", -16);
        m1.addAirport(a1);
        m1.addCheckpoint(c2);
        m1.addAirport(a3);
        m1.addCheckpoint(c4);
        m1.addAirport(a6);
        m1.addCheckpoint(c7);
        m1.addAirport(a2);
        m1.addCheckpoint(c5);
    }

    protected void setUpAircraft() {
        ac = new Aircraft("GMSD", 24.5, 5.4, 90);
    }

    protected void checkCheckpoint(List<Leg> legs, Checkpoint checkpoint) {
        assertEquals(legs.size(), checkpoint.getLegs().size());
        for (Leg l : legs) {
            String name = l.getDestName();
            assertNotNull(checkpoint.getLegTo(name));
            assertEquals(l.getHeading(), checkpoint.getLegTo(name).getHeading());
            assertEquals(l.getDistance(), checkpoint.getLegTo(name).getDistance());
        }
    }

    protected void checkAirport(int arrivalAlt, Airport airport) {
        assertEquals(arrivalAlt, airport.getArrivalAlt());
    }

    protected void checkRunways(List<Runway> runways) {
        for (int k = 0; k < runways.size(); k++) {
            assertEquals(this.runways.get(k).getName(), runways.get(k).getName());
            assertEquals(this.runways.get(k).getCondition(), runways.get(k).getCondition());
        }
    }

    protected void checkMap(Map map) {
        assertEquals("Lower Mainland", map.getRegion());
        assertEquals(-16, map.getMagVar());
        assertEquals(8, map.getCheckpoints().size());
        assertEquals(4, map.getAirports().size());
        checkCheckpoint(c5.getLegs(), map.getCheckpoint("Port Moody"));
        checkCheckpoint(a2.getLegs(), map.getCheckpoint("Pitt Meadows"));
        checkCheckpoint(c4.getLegs(), map.getCheckpoint("Fort Langley"));
        checkCheckpoint(c2.getLegs(), map.getCheckpoint("Keefers"));
        checkCheckpoint(a1.getLegs(), map.getCheckpoint("Hope"));
        checkCheckpoint(a3.getLegs(), map.getCheckpoint("Chilliwack"));
        checkCheckpoint(c7.getLegs(), map.getCheckpoint("Greenhouse"));
        checkCheckpoint(a6.getLegs(), map.getCheckpoint("Abbotsford"));
        checkAirport(a2.getArrivalAlt(), map.getAirport("CYPK"));
        checkAirport(a1.getArrivalAlt(), map.getAirport("CYHE"));
        checkAirport(a3.getArrivalAlt(), map.getAirport("CYCW"));
        checkAirport(a6.getArrivalAlt(), map.getAirport("CYXX"));
        checkRunways(map.getAirport("CYPK").getRunways());
    }

    protected void checkFlightPlan(FlightPlan expect, FlightPlan test) {
        assertEquals(m1.getRegion(), test.getMap().getRegion());
        assertEquals(ac.getRegistration(), test.getAircraft().getRegistration());
        assertEquals(expect.getDeparture().getName(), test.getDeparture().getName());
        assertEquals(expect.getDestination().getName(), test.getDestination().getName());
        assertEquals(expect.getEnRoutePoints().size(), test.getEnRoutePoints().size());
        assertEquals(expect.getCpNames().size(), test.getCpNames().size());
        for (int k = 0; k < expect.getCpNames().size(); k++) {
            assertEquals(expect.getCpNames().get(k), test.getCpNames().get(k));
        }
    }
}
