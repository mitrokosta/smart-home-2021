package ru.sbt.mipt.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rc.RemoteControl;
import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.alarm.Alarm;
import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.rc.*;
import ru.sbt.mipt.oop.util.Counter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    SmartHome testHome;
    Alarm alarm;
    RemoteControl rc;
    Counter counter;

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
        alarm = new Alarm();
        counter = new Counter();
        ConfigurableRemoteControl crc = new ConfigurableRemoteControl(List.of("A", "B", "C", "D", "1", "2", "3", "4"));

        crc.setCommandForButton("A", new RaiseAlarmCommand(alarm));
        crc.setCommandForButton("B", new TurnHallDoorLightOnCommand(testHome));
        crc.setCommandForButton("C", new TurnAllLightOnCommand(testHome));
        crc.setCommandForButton("D", new TurnAllLightOffCommand(testHome));
        crc.setCommandForButton("1", new CloseHallDoorCommand(testHome));
        crc.setCommandForButton("2", new ActivateAlarmCommand(alarm, "123"));

        rc = crc;
    }

    @Test
    void testRaiseAlarm() {
        assertTrue(alarm.isDeactivated());
        rc.onButtonPressed("A");
        assertTrue(alarm.isRaised());
    }

    @Test
    void testTurnHallDoorLightOn() {
        List<Light> lights = List.of(getLightById("7"), getLightById("8"), getLightById("9"));

        lights.forEach(light -> assertFalse(light.isOn()));
        rc.onButtonPressed("B");
        lights.forEach(light -> assertTrue(light.isOn()));
    }

    @Test
    void testAllLightOnCommand() {
        Collection<Light> lights = getAllLights();

        rc.onButtonPressed("C");
        lights.forEach(light -> assertTrue(light.isOn()));
    }

    @Test
    void testAllLightOffCommand() {
        Collection<Light> lights = getAllLights();

        rc.onButtonPressed("D");
        lights.forEach(light -> assertFalse(light.isOn()));
    }

    @Test
    void testCloseHallDoorCommand() {
        Door hallDoor = getDoorById("4");

        assertTrue(hallDoor.isOpen());
        rc.onButtonPressed("1");
        assertFalse(hallDoor.isOpen());
    }

    @Test
    void testActivateAlarmCommand() {
        assertTrue(alarm.isDeactivated());
        rc.onButtonPressed("2");
        assertTrue(alarm.isActivated());

        alarm.deactivate("123"); // проверка что поставлен верный пароль
        assertTrue(alarm.isDeactivated());
    }

    @Test
    void testValidButtonForRemoteControlExpectExecutedCommand() {
        assertEquals(0, counter.getCount());
        rc.onButtonPressed("3"); // проверка что ничего пока что не происходит
        assertEquals(0, counter.getCount());

        ConfigurableRemoteControl crc = (ConfigurableRemoteControl) rc;
        crc.setCommandForButton("3", () -> counter.add());

        rc.onButtonPressed("3");
        assertEquals(1, counter.getCount());
    }

    @Test
    void testInvalidButtonForRemoteControlExpectIgnoredCommand() {
        assertEquals(0, counter.getCount());

        ConfigurableRemoteControl crc = (ConfigurableRemoteControl) rc;
        crc.setCommandForButton("Z", () -> counter.add());

        rc.onButtonPressed("Z");
        assertEquals(0, counter.getCount());
    }

    @Test
    void testDefaultCommandConfiguration() {
        ConfigurableRemoteControl crc = new ConfigurableRemoteControl(List.of("A"), () -> counter.add());

        assertEquals(0, counter.getCount());
        crc.onButtonPressed("A");
        assertEquals(1, counter.getCount());
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

    Collection<Light> getAllLights() {
        List<Light> lights = new ArrayList<>();

        testHome.execute((Actionable lightObject) -> {
            if (lightObject instanceof Light) {
                lights.add((Light) lightObject);
            }
        });

        return lights;
    }

    Collection<Door> getAllDoors() {
        List<Door> doors = new ArrayList<>();

        testHome.execute((Actionable doorObject) -> {
            if (doorObject instanceof Door) {
                doors.add((Door) doorObject);
            }
        });

        return doors;
    }
}