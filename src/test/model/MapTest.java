package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private Map m1;
    private Map m2;

    private Checkpoint c2;
    private Checkpoint c4;
    private Checkpoint c5;
    private Airport a1;
    private Airport a2;
    private Airport a3;

    private Leg l1;
    private Leg l2r;
    private Leg l3r;
    private Leg l4r;
    private Leg l5;

    @BeforeEach
    public void setUp() {
        List<Runway> runways;
        Runway r1;
        Runway r2;

        runways = new ArrayList<>();
        r1 = new Runway(26);
        r2 = new Runway(6);
        runways.add(r1);
        runways.add(r2);

        c2 = new Checkpoint("Keefers", new ArrayList<>());
        a3 = new Airport("Chilliwack", new ArrayList<>(),"CYCW", 1500, runways);
        c4 = new Checkpoint("Fort Langley", new ArrayList<>());

        a1 = new Airport("Hope", new ArrayList<>(), "CYHE");

        l2r = new Leg(a1, 175, 38);
        l3r = new Leg(a1, 55, 24);
        l4r = new Leg(a1, 74, 49);
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
        l1 = new Leg(a2, 46, 7);
        legs.add(l1);
        c5 = new Checkpoint("Port Moody", legs);

        m2 = new Map("Burrard Inlet", -20);
        m2.addAirport(a2);
        m2.addCheckpoint(c5);
    }

    @Test
    public void testInit() {
        assertEquals(m1.getRegion(), "Mountains");
        assertEquals(m2.getRegion(), "Burrard Inlet");
        assertEquals(m1.getMagVar(), -16);
        assertEquals(m2.getMagVar(), -20);
        assertEquals(m1.getCheckpoints().size(), 4);
        assertEquals(m2.getCheckpoints().size(), 2);
        assertEquals(m1.getAirports().size(), 2);
        assertEquals(m2.getAirports().size(), 1);
    }

    @Test
    public void testGetCheckpointSuccess() {
        assertEquals(m1.getCheckpoint("Hope"), a1);
        assertEquals(m1.getCheckpoint("Keefers"), c2);
        assertEquals(m1.getCheckpoint("Chilliwack"), a3);
        assertEquals(m1.getCheckpoint("Fort Langley"), c4);

        assertEquals(m2.getCheckpoint("Pitt Meadows"), a2);
        assertEquals(m2.getCheckpoint("Port Moody"), c5);
    }

    @Test
    public void testGetCheckpointNull() {
        assertNull(m1.getCheckpoint("Cashe Creek"));
        assertNull(m1.getCheckpoint("Lions Gate"));
        assertNull(m2.getCheckpoint("Bowser"));
        assertNull(m2.getCheckpoint(null));
    }

    @Test
    public void testGetAirportSuccess() {
        assertEquals(m1.getAirport("CYHE"), a1);
        assertEquals(m1.getAirport("CYCW"), a3);

        assertEquals(m2.getAirport("CYPK"), a2);
    }

    @Test
    public void testGetAirportNull() {
        assertNull(m1.getAirport("CYVR"));
        assertNull(m1.getAirport("CZBB"));
        assertNull(m2.getAirport("CYHE"));
        assertNull(m2.getAirport(null));
    }

    @Test
    public void testAddCheckpointSuccess() {
        List<Leg> legs = new ArrayList<>();
        Leg la = new Leg(c5, 180, 13);
        Leg lb = new Leg(a2, 360, 17);
        legs.add(la);
        legs.add(lb);
        Checkpoint ca = new Checkpoint("Lions Gate", legs);
        m2.addCheckpoint(ca);

        assertEquals(m2.getCheckpoints().size(), 3);
        assertEquals(m2.getAirports().size(), 1);
        assertEquals(m2.getCheckpoint("Lions Gate"), ca);
        assertEquals(m2.getCheckpoint("Port Moody").getLegTo("Lions Gate").getHeading(), 360);
        assertEquals(m2.getAirport("CYPK").getLegTo("Lions Gate").getHeading(), 180);
        assertEquals(m2.getCheckpoint("Port Moody").getLegTo("Lions Gate").getDistance(), 13);
        assertEquals(m2.getCheckpoint("Pitt Meadows").getLegTo("Lions Gate").getDistance(), 17);
    }

    @Test
    public void testAddCheckpointFail() {
        List<Leg> legs = new ArrayList<>();
        legs.add(l1);
        Checkpoint ca = new Checkpoint("Port Moody", new ArrayList<>());
        Checkpoint cb = new Checkpoint("Pitt Meadows", legs);
        m2.addCheckpoint(ca);
        m2.addCheckpoint(cb);

        assertEquals(m2.getCheckpoints().size(), 2);
        assertEquals(m2.getCheckpoint("Pitt Meadows"), a2);
        assertEquals(m2.getCheckpoint("Port Moody"), c5);
    }

    @Test
    public void testAddAirportSuccess() {
        List<Leg> legsa = new ArrayList<>();
        List<Leg> legsb = new ArrayList<>();
        Leg la = new Leg(c5, 181, 40);
        Leg lb = new Leg(a2, 359, 10);
        Leg lc = new Leg(a2, 1, 1);
        legsa.add(la);
        legsa.add(lb);
        legsb.add(lc);
        Airport aa = new Airport("Sechelt", legsa, "CAP3");
        m2.addAirport(aa);
        Airport ab = new Airport("Boundary Bay", legsb, "CZBB");
        m2.addAirport(ab);

        assertEquals(m2.getCheckpoints().size(), 4);
        assertEquals(m2.getAirports().size(), 3);
        assertEquals(m2.getCheckpoint("Sechelt"), aa);
        assertEquals(m2.getCheckpoint("Boundary Bay"), ab);
        assertEquals(m2.getAirport("CAP3"), aa);
        assertEquals(m2.getAirport("CZBB"), ab);

        assertEquals(m2.getCheckpoint("Port Moody").getLegTo("Sechelt").getHeading(), 1);
        assertEquals(m2.getAirport("CYPK").getLegTo("Sechelt").getHeading(), 179);
        assertEquals(m2.getAirport("CYPK").getLegTo("Boundary Bay").getHeading(), 181);
        assertEquals(m2.getCheckpoint("Port Moody").getLegTo("Sechelt").getDistance(), 40);
        assertEquals(m2.getAirport("CYPK").getLegTo("Sechelt").getDistance(), 10);
        assertEquals(m2.getCheckpoint("Pitt Meadows").getLegTo("Boundary Bay").getDistance(), 1);
    }

    @Test
    public void testAddAirportFail() {
        List<Leg> legs = new ArrayList<>();
        legs.add(l1);
        Airport aa = new Airport("Pitt Meadows", legs, "CYPK");
        m2.addAirport(aa);
        assertEquals(m2.getCheckpoints().size(), 2);
        assertEquals(m2.getCheckpoint("Pitt Meadows"), a2);
        assertEquals(m2.getCheckpoint("Port Moody"), c5);
    }

    @Test
    public void testAddLegTo() {
        Leg la = new Leg(c4, 200, 42);
        m1.addLegTo("Keefers", la);
        assertEquals(m1.getCheckpoint("Keefers").getLegs().size(), 2);
        assertEquals(m1.getCheckpoint("Keefers").getLegTo("Fort Langley"), la);
        assertEquals(m1.getCheckpoint("Fort Langley").getLegs().size(), 3);
        assertEquals(m1.getCheckpoint("Fort Langley").getLegTo("Keefers").getHeading(), 20);
        assertEquals(m1.getCheckpoint("Fort Langley").getLegTo("Keefers").getDistance(), 42);
    }

    @Test
    public void testFindRouteSelf() {
        List<String> route = m2.findRoute("Pitt Meadows", "Pitt Meadows");
        assertEquals(route.size(), 1);
        assertEquals(route.get(0), "Pitt Meadows");
    }

    @Test
    public void testFindRouteSmall() {
        List<String> route = m2.findRoute("Port Moody", "Pitt Meadows");
        assertEquals(route.size(), 2);
        assertEquals(route.get(0), "Port Moody");
        assertEquals(route.get(1), "Pitt Meadows");
    }

    @Test
    public void testFindRouteDirect() {
        List<String> route = m1.findRoute("Hope", "Chilliwack");
        assertEquals(route.size(), 2);
        assertEquals(route.get(0), "Hope");
        assertEquals(route.get(1), "Chilliwack");
    }

    @Test
    public void testFindRouteLong() {
        List<String> route = m1.findRoute("Keefers", "Chilliwack");
        assertEquals(route.size(), 3);
        assertEquals(route.get(0), "Keefers");
        assertEquals(route.get(1), "Hope");
        assertEquals(route.get(2), "Chilliwack");
    }

    @Test
    public void testFindRouteAround() {
        List<String> route = m1.findRoute("Fort Langley", "Keefers");
        assertEquals(route.size(), 4);
        assertEquals(route.get(0), "Fort Langley");
        assertEquals(route.get(1), "Chilliwack");
        assertEquals(route.get(2), "Hope");
        assertEquals(route.get(3), "Keefers");
    }

    @Test
    public void testFindRouteNone() {
        assertNull(m1.findRoute("Fort Langley", "Tofino"));
    }

    @Test
    public void testGetLegsBetweenSelf() {
        List<Leg> legs = m2.getLegsBetween(m2.findRoute("Pitt Meadows", "Pitt Meadows"));
        assertEquals(legs.size(), 0);
    }

    @Test
    public void testGetLegsBetweenShort() {
        List<Leg> legs = m2.getLegsBetween(m2.findRoute("Port Moody", "Pitt Meadows"));
        assertEquals(legs.size(), 1);
        assertEquals(legs.get(0), l1);
    }

    @Test
    public void testGetLegsBetweenLong() {
        List<Leg> legs = m1.getLegsBetween(m1.findRoute("Keefers", "Fort Langley"));
        assertEquals(legs.size(), 3);
        assertEquals(legs.get(0).getDestName(), "Hope");
        assertEquals(legs.get(1).getDestName(), "Chilliwack");
        assertEquals(legs.get(2).getDestName(), "Fort Langley");
    }
}