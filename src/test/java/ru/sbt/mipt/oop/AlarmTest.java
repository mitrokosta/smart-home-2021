package ru.sbt.mipt.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.sbt.mipt.oop.alarm.*;
import ru.sbt.mipt.oop.event.*;
import ru.sbt.mipt.oop.handler.AlarmActivateEventHandler;
import ru.sbt.mipt.oop.handler.AlarmDeactivateEventHandler;
import ru.sbt.mipt.oop.handler.CountingEventHandler;
import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.util.Counter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlarmTest {
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
    void testChangeAlarmState() {
        Alarm alarm = new Alarm();

        assertTrue(alarm.isDeactivated());

        alarm.activate("123");
        assertTrue(alarm.isActivated());

        alarm.raise();
        assertTrue(alarm.isRaised());
    }

    @Test
    void testWrongCodeForActivatedAlarmExpectRaisedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.isActivated());

        alarm.deactivate("456");
        assertTrue(alarm.isRaised());
    }

    @Test
    void testRaisedAlarmProtectionExpectIgnoredHandlers() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        alarm.raise();
        assertTrue(alarm.isRaised());

        Counter counter = new Counter();
        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new CountingEventHandler(counter)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new SensorEvent(EventType.DOOR_OPEN, "1"),
                new SensorEvent(EventType.LIGHT_OFF, "1"),
                new AlarmEvent(EventType.ALARM_DEACTIVATE, "123")
        )));

        eventProcessor = new AlarmIgnoringProtector(eventProcessor, alarm, new SmsIntrusionNotifier()); // apply protection

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertEquals(0, counter.getCount());
    }

    @Test
    void testActivatedAlarmIntrusionExpectRaisedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.isActivated());

        EventProcessor eventProcessor = new HandlingEventProcessor(new ArrayList<>());

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new SensorEvent(EventType.DOOR_OPEN, "1")
        )));

        eventProcessor = new AlarmIntrusionDetector(eventProcessor, alarm); // apply protection

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.isRaised());
    }

    @Test
    void testAlarmActivateHandlerExpectActivatedAlarm() {
        Alarm alarm = new Alarm();

        assertTrue(alarm.isDeactivated());

        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new AlarmActivateEventHandler(alarm)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new AlarmEvent(EventType.ALARM_ACTIVATE, "123")
        )));

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.isActivated());
    }

    @Test
    void testAlarmDeactivateHandlerExpectDeactivatedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.isActivated());

        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new AlarmDeactivateEventHandler(alarm)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new AlarmEvent(EventType.ALARM_DEACTIVATE, "123")
        )));

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.isDeactivated());
    }

    @Test
    void testAlarmDeactivateHandlerWithWrongCodeExpectRaisedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.isActivated());

        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new AlarmDeactivateEventHandler(alarm)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new AlarmEvent(EventType.ALARM_DEACTIVATE, "456")
        )));

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.isRaised());
    }
}
