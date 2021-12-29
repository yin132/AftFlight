package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RunwayTest {
    Runway r1;
    Runway r2;
    Runway r3;
    Runway r4;

    @BeforeEach
    public void setUp() {
        r1 = new Runway(18);
        r2 = new Runway(19, 'R');
        r3 = new Runway(36, 'C', 4500, 100, 'H');
        r4 = new Runway(1, 2500, 75, 'S');
    }

    @Test
    public void testGetHeadings() {
        assertEquals(r1.getEastHeading(), 180);
        assertEquals(r1.getWestHeading(), 360);

        assertEquals(r2.getEastHeading(), 10);
        assertEquals(r2.getWestHeading(), 190);

        assertEquals(r3.getEastHeading(), 180);
        assertEquals(r3.getWestHeading(), 360);

        assertEquals(r4.getEastHeading(), 10);
        assertEquals(r4.getWestHeading(), 190);
    }

    @Test
    public void testGetters() {
        assertEquals(r3.getLength(), 4500);
        assertEquals(r4.getLength(), 2500);
        assertEquals(r3.getWidth(), 100);
        assertEquals(r4.getWidth(), 75);
    }

    @Test
    public void testGetEastName() {
        assertEquals(r1.getEastName(), "18");
        assertEquals(r2.getEastName(), "01L");
        assertEquals(r3.getEastName(), "18C");
        assertEquals(r4.getEastName(), "01");
    }

    @Test
    public void testGetWestName() {
        assertEquals(r1.getWestName(), "36");
        assertEquals(r2.getWestName(), "19R");
        assertEquals(r3.getWestName(), "36C");
        assertEquals(r4.getWestName(), "19");
    }

    @Test
    public void testGetName() {
        assertEquals(r1.getName(), "18/36");
        assertEquals(r2.getName(), "01L/19R");
        assertEquals(r3.getName(), "18C/36C");
        assertEquals(r4.getName(), "01/19");
    }

    @Test
    public void testGetCondition() {
        assertEquals(r1.getCondition(), "");
        assertEquals(r3.getCondition(), "4500x100 H");
        assertEquals(r4.getCondition(), "2500x75 S");
    }

    @Test
    public void testNotes() {
        r1.setNotes("obstacles on 36 approach end");
        r2.setNotes(null);
        r3.setNotes("");
        r4.setNotes("very bumpy");
        assertEquals(r1.getNotes(), "obstacles on 36 approach end");
        assertNull(r2.getNotes());
        assertEquals(r3.getNotes(), "");
        assertEquals(r4.getNotes(), "very bumpy");
        assertEquals(r1.getCondition(), "obstacles on 36 approach end");
        assertEquals(r2.getCondition(), "");
        assertEquals(r3.getCondition(), "4500x100 H");
        assertEquals(r4.getCondition(), "2500x75 S very bumpy");
    }

    @Test
    public void testInvalidSide() {
        r1 = new Runway(24, 'Z');
        r2 = new Runway(12, 'X', 1200, 100, 'S');
        assertEquals(r1.getName(), "06/24");
        assertEquals(r2.getName(), "12/30");
    }

    @Test
    public void testConstructorSideBranches() {
        r1 = new Runway(24, 'L');
        r2 = new Runway(24, 'R');
        r3 = new Runway(24, 'C');
        assertEquals(r1.getName(), "06R/24L");
        assertEquals(r2.getName(), "06L/24R");
        assertEquals(r3.getName(), "06C/24C");

        r1 = new Runway(24, 'L', 5000, 75, 'H');
        r2 = new Runway(24, 'R', 5000, 75, 'H');
        r3 = new Runway(24, 'C', 5000, 75, 'H');
        assertEquals(r1.getName(), "06R/24L");
        assertEquals(r2.getName(), "06L/24R");
        assertEquals(r3.getName(), "06C/24C");
    }
}
