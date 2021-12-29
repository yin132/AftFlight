package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckpointTest {
    protected Checkpoint c1;
    protected Checkpoint c2;
    protected Checkpoint c3;
    protected Checkpoint c4;
    protected Leg l2;
    protected Leg l3;
    protected Leg l2r;
    protected Leg l3r;
    protected Leg l4;
    protected Leg l4r;
    protected Leg l5;
    protected Leg l5r;

    @BeforeEach
    public void setUp() {
        List<Leg> legs = new ArrayList<>();

        c2 = new Checkpoint("Keefers", new ArrayList<>());
        c3 = new Checkpoint("Chilliwack", new ArrayList<>());
        c4 = new Checkpoint("Fort Langley", new ArrayList<>());

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
    }

    @Test
    public void testConstructor() {
        assertEquals(c1.getName(), "Hope");
        assertEquals(c2.getName(), "Keefers");
        assertEquals(c3.getName(), "Chilliwack");
        assertEquals(c4.getName(), "Fort Langley");

        assertEquals(c1.getLegs().size(),3);
        assertEquals(c1.getLegs().get(0),l2);
        assertEquals(c1.getLegs().get(1),l3);
        assertEquals(c1.getLegs().get(2),l4);

        assertEquals(c2.getLegs().size(),1);
        assertEquals(c2.getLegs().get(0),l2r);

        assertEquals(c3.getLegs().size(),2);
        assertEquals(c3.getLegs().get(0),l3r);
        assertEquals(c3.getLegs().get(1), l5r);

        assertEquals(c4.getLegs().size(),2);
        assertEquals(c4.getLegs().get(0),l4r);
        assertEquals(c4.getLegs().get(1), l5);
    }

    @Test
    public void testAddLegFail() {
        Leg r = new Leg(c2,355,38);
        assertFalse(c1.addLeg(r));
        assertEquals(c1.getLegs().size(), 3);
    }

    @Test
    public void testGetLegToFound() {
        assertEquals(c1.getLegTo("Keefers"), l2);
        assertEquals(c2.getLegTo("Hope"), l2r);
        assertEquals(c1.getLegTo("Fort Langley"), l4);
        assertEquals(c3.getLegTo("Fort Langley"), l5r);
    }

    @Test
    public void testGetLegToFailed() {
        assertNull(c1.getLegTo("Abbotsford"));
        assertNull(c2.getLegTo("Pitt Meadows"));
        assertNull(c3.getLegTo("Keefers"));
    }
}
