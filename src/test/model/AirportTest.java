package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AirportTest extends CheckpointTest {
    private Airport a1;
    private List<Runway> runways;
    private Runway r1;
    private Runway r2;

    @Override
    @BeforeEach
    public void setUp() {
        List<Leg> legs = new ArrayList<>();
        runways = new ArrayList<>();
        r1 = new Runway(26);
        r2 = new Runway(6);
        runways.add(r1);
        runways.add(r2);

        c2 = new Checkpoint("Keefers", new ArrayList<>());
        c3 = new Airport("Chilliwack", new ArrayList<>(),"CYCW", 1500);
        c4 = new Airport("Fort Langley", new ArrayList<>(), "CBQ2", 2000, runways);

        l2 = new Leg(c2,355,38);
        l3 = new Leg(c3, 235, 24);
        l4 = new Leg(c4, 254,44);
        legs.add(l2);
        legs.add(l3);
        legs.add(l4);
        c1 = new Airport("Hope", legs, "CYHE");

        l2r = new Leg(c1, 175, 38);
        l3r = new Leg(c1, 55, 24);
        l4r = new Leg(c1, 74, 44);
        c2.addLeg(l2r);
        c3.addLeg(l3r);
        c4.addLeg(l4r);

        l5 = new Leg(c3, 92, 24);
        l5r = new Leg(c4, 272, 24);
        c4.addLeg(l5);
        c3.addLeg(l5r);

        a1 = new Airport("Pitt Meadows", new ArrayList<>(), "CYPK", 1500, runways);
    }

    @Test
    public void testAirportConstructor() {
        assertEquals(a1.getCode(), "CYPK");
        assertEquals(a1.getArrivalAlt(), 1500);
        assertEquals(a1.getRunways().size(), 2);
        assertEquals(a1.getRunways().get(0), r1);
        assertEquals(a1.getRunways().get(1), r2);
    }

    @Test
    public void testAddRunwayFalse() {
        assertFalse(a1.addRunway(r1));
        assertEquals(a1.getRunways().size(), 2);
        assertFalse(a1.addRunway(new Runway(6)));
        assertEquals(a1.getRunways().size(), 2);
    }

    @Test
    public void testAddRunwayTrue() {
        Runway r3 = new Runway(1);
        Runway r4 = new Runway(7,'L');
        assertTrue(a1.addRunway(r3));
        assertEquals(a1.getRunways().size(), 3);
        assertEquals(a1.getRunways().get(2), r3);
        assertTrue(a1.addRunway(r4));
        assertEquals(a1.getRunways().size(), 4);
        assertEquals(a1.getRunways().get(3), r4);
    }
}