package ru.sbt.mipt.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.sbt.mipt.oop.command.DummyCommandSender;
import ru.sbt.mipt.oop.home.*;
import ru.sbt.mipt.oop.sensor.*;
import ru.sbt.mipt.oop.sensor.handler.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

class SmartHomeTest {
    SmartHome testHome;

    @BeforeEach
    void setUp() {
        Room kitchen = new Room(Arrays.asList(new Light("1", false), new Light("2", true)),
                Arrays.asList(new Door(false, "1")),
                "kitchen");
        Room bathroom = new Room(Arrays.asList(new Light("3", true)),
                Arrays.asList(new Door(false, "2")),
                "bathroom");
        Room bedroom = new Room(Arrays.asList(new Light("4", false), new Light("5", false), new Light("6", false)),
                Arrays.asList(new Door(true, "3")),
                "bedroom");
        Room hall = new Room(Arrays.asList(new Light("7", false), new Light("8", false), new Light("9", false)),
                Arrays.asList(new Door(true, "4")),
                "hall");
        testHome = new SmartHome(Arrays.asList(kitchen, bathroom, bedroom, hall));
    }

    @Test
    void testLightOnHandler() {
        Light light = getLightById("1");

        assertFalse(light.isOn());

        processEvents(List.of(
                new SensorEvent(SensorEventType.LIGHT_ON, "1")
        ), List.of(
                new LightOnSensorEventHandler(testHome)
        ));

        assertTrue(light.isOn());
    }

    @Test
    void testLightOffHandler() {
        Light light = getLightById("2");

        assertTrue(light.isOn());

        processEvents(List.of(
                new SensorEvent(SensorEventType.LIGHT_OFF, "2")
        ), List.of(
                new LightOffSensorEventHandler(testHome)
        ));

        assertFalse(light.isOn());
    }

    @Test
    void testDoorOpenedHandler() {
        Door door = getDoorById("1");

        assertFalse(door.isOpen());

        processEvents(List.of(
                new SensorEvent(SensorEventType.DOOR_OPEN, "1")
        ), List.of(
                new DoorOpenedSensorEventHandler(testHome)
        ));

        assertTrue(door.isOpen());
    }

    @Test
    void testDoorClosedHandler() {
        Door door = getDoorById("3");

        assertTrue(door.isOpen());

        processEvents(List.of(
                new SensorEvent(SensorEventType.DOOR_CLOSED, "3")
        ), List.of(
                new DoorClosedSensorEventHandler(testHome)
        ));

        assertFalse(door.isOpen());
    }

    @Test
    void testHallDoorClosedHandler() {
        Door door = getDoorById("4");

        assertTrue(door.isOpen());

        Light light = getLightById("2");

        assertTrue(light.isOn());

        processEvents(List.of(
                new SensorEvent(SensorEventType.DOOR_CLOSED, "4")
        ), List.of(
                new DoorClosedSensorEventHandler(testHome),
                new HallDoorClosedSensorEventHandler(testHome, new DummyCommandSender())
        ));

        assertFalse(door.isOpen());

        assertFalse(light.isOn());
    }

    @Test
    void testActionableCount() {
        var ref = new Object() {
            int objCount = 0;
        };

        testHome.execute((Actionable obj) -> {
            ref.objCount++;
        });

        assertEquals(18, ref.objCount);
    }

    void processEvents(List<SensorEvent> events, List<SensorEventHandler> handlers) {
        SensorEventProcessor sensorEventProcessor = new HandlingSensorEventProcessor(handlers);
        SensorEventQueue eventQueue = new PredefinedSensorEventQueue(new ArrayDeque<>(events));

        SensorEventLoop eventLoop = new SensorEventLoop(eventQueue, sensorEventProcessor);
        eventLoop.start();
    }

    Light getLightById(String id) {
        var ref = new Object() {
            Light light = null;
        };

        testHome.execute((Actionable lightObject) -> {
            if (lightObject instanceof Light) {
                Light temp = (Light) lightObject;
                if (temp.getId().equals(id)) {
                    ref.light = temp;
                }
            }
        });

        return ref.light;
    }

    Door getDoorById(String id) {
        var ref = new Object() {
            Door door = null;
        };

        testHome.execute((Actionable doorObject) -> {
            if (doorObject instanceof Door) {
                Door temp = (Door) doorObject;
                if (temp.getId().equals(id)) {
                    ref.door = temp;
                }
            }
        });

        return ref.door;
    }
}