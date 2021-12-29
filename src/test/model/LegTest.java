package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LegTest {
    private Leg leg1;
    private Leg leg2;
    private Checkpoint cp1;
    private Checkpoint cp2;

    @BeforeEach
    public void setUp() {
        cp1 = new Checkpoint("Albion", new ArrayList<>());
        cp2 = new Airport("Hope", new ArrayList<>(), "CYHE");
        leg1 = new Leg(cp1, 70, 20);
        leg2 = new Leg(cp2, 359, 35);
    }

    @Test
    public void testConstructor() {
        assertEquals(leg1.getDestination(), cp1);
        assertEquals(leg1.getHeading(), 70);
        assertEquals(leg1.getDistance(), 20);

        assertEquals(leg2.getDestination(), cp2);
        assertEquals(leg2.getHeading(), 359);
        assertEquals(leg2.getDistance(), 35);
    }

    @Test
    public void testGetDestName() {
        assertEquals(leg1.getDestName(), "Albion");
        assertEquals(leg2.getDestName(), "Hope");
    }
}
