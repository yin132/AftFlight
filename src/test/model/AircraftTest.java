package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AircraftTest {
    private Aircraft ac1;
    private Aircraft ac2;

    @BeforeEach
    public void setUp() {
        ac1 = new Aircraft("GMSD", 24.5, 5.4, 90);
        ac2 = new Aircraft("GMUF", 40, 6.7,114);
    }

    @Test
    public void testConstructor() {
        assertEquals(ac1.getRegistration(), "GMSD");
        assertEquals(ac1.getFuel(), 24.5);
        assertEquals(ac1.getFuelBurn(), 5.4);
        assertEquals(ac1.getCruiseSpeed(), 90);

        assertEquals(ac2.getRegistration(), "GMUF");
        assertEquals(ac2.getFuel(), 40);
        assertEquals(ac2.getFuelBurn(), 6.7);
        assertEquals(ac2.getCruiseSpeed(), 114);
    }

    @Test
    public void testSetters() {
        ac1.setFuel(30);
        assertEquals(ac1.getFuel(), 30);
    }
}
