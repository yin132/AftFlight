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
class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyMap() {
        try {
            Map map = new Map("Mountains", -12);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMap.json");
            writer.open();
            writer.write(map);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMap.json");
            map = reader.readMap();
            assertEquals("Mountains", map.getRegion());
            assertEquals(-12, map.getMagVar());
            assertEquals(0, map.getCheckpoints().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriteAircraft() {
        setUpAircraft();
        Aircraft aircraft;
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterAircraft.json");
            writer.open();
            writer.write(ac);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterAircraft.json");
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
    void testWriterGeneralMap() {
        setUpMap();

        try {
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMap.json");
            writer.open();
            writer.write(m1);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMap.json");
            Map map = reader.readMap();
            checkMap(map);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriteFlightPlanDirect() {
        setUpAircraft();
        setUpMap();
        FlightPlan flightPlan = new FlightPlan(ac, m1, "CYCW", "CYPK");

        try {
            JsonWriter writer = new JsonWriter("./data/testWriterFPDirect.json");
            writer.open();
            writer.write(flightPlan);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFPDirect.json");
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
            JsonWriter writer = new JsonWriter("./data/testWriterFPAround.json");
            writer.open();
            writer.write(flightPlan);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFPAround.json");
            FlightPlan tfp = reader.readFlightPlan();
            checkFlightPlan(flightPlan, tfp);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}