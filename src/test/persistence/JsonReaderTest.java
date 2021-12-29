package persistence;

import model.Aircraft;
import model.FlightPlan;
import model.Map;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Code obtained and modified from JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Map map = reader.readMap();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReadAircraft() {
        Aircraft aircraft;
        JsonReader reader = new JsonReader("./data/testReaderAircraft.json");
        try {
            aircraft = reader.readAircraft();
            assertEquals("GMSD", aircraft.getRegistration());
            assertEquals(24.5, aircraft.getFuel());
            assertEquals(5.4, aircraft.getFuelBurn());
            assertEquals(90, aircraft.getCruiseSpeed());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testReaderEmptyMap() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMap.json");
        try {
            Map map = reader.readMap();
            assertEquals("Mountains", map.getRegion());
            assertEquals(-12, map.getMagVar());
            assertEquals(0, map.getCheckpoints().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralMap() {
        setUpMap();

        JsonReader reader = new JsonReader("./data/testReaderGeneralMap.json");
        try {
            Map map = reader.readMap();
            checkMap(map);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderFlightPlanDirect() {
        setUpAircraft();
        setUpMap();
        FlightPlan flightPlan = new FlightPlan(ac, m1, "CYCW", "CYPK");
        try {
            JsonReader reader = new JsonReader("./data/testReaderFPDirect.json");
            FlightPlan tfp = reader.readFlightPlan();
            checkFlightPlan(flightPlan, tfp);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriteFlightPlanAround() {
        setUpAircraft();
        setUpMap();
        FlightPlan flightPlan = new FlightPlan(ac, m1, "CYCW", "CYPK");
        flightPlan.addEnRoutePoint("Abbotsford");
        try {
            JsonReader reader = new JsonReader("./data/testReaderFPAround.json");
            FlightPlan tfp = reader.readFlightPlan();
            checkFlightPlan(flightPlan, tfp);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}