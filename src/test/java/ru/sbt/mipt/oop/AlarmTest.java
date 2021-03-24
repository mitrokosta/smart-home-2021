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

        assertTrue(alarm.getState() instanceof AlarmDeactivated);

        alarm.activate("123");
        assertTrue(alarm.getState() instanceof AlarmActivated);

        alarm.raise();
        assertTrue(alarm.getState() instanceof AlarmRaised);
    }

    @Test
    void testWrongCodeForActivatedAlarmExpectRaisedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.getState() instanceof AlarmActivated);

        alarm.deactivate("456");
        assertTrue(alarm.getState() instanceof AlarmRaised);
    }

    @Test
    void testRaiseDeactivatedAlarmExpectDeactivatedAlarm() {
        Alarm alarm = new Alarm();

        alarm.deactivate("123");
        assertTrue(alarm.getState() instanceof AlarmDeactivated);

        alarm.raise();
        assertTrue(alarm.getState() instanceof AlarmDeactivated);
    }

    @Test
    void testRaisedAlarmProtectionExpectIgnoredHandlers() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        alarm.raise();
        assertTrue(alarm.getState() instanceof AlarmRaised);

        Counter counter = new Counter();
        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new CountingEventHandler(counter)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new SensorEvent(EventType.DOOR_OPEN, "1"),
                new SensorEvent(EventType.LIGHT_OFF, "1"),
                new AlarmEvent(EventType.ALARM_DEACTIVATE, "123")
        )));

        eventProcessor = new AlarmIgnoringProtector(eventProcessor, alarm); // apply protection

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertEquals(0, counter.getCount());
    }

    @Test
    void testActivatedAlarmIntrusionExpectRaisedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.getState() instanceof AlarmActivated);

        EventProcessor eventProcessor = new HandlingEventProcessor(new ArrayList<>());

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new SensorEvent(EventType.DOOR_OPEN, "1")
        )));

        eventProcessor = new AlarmIntrusionDetector(eventProcessor, alarm); // apply protection

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.getState() instanceof AlarmRaised);
    }

    @Test
    void testAlarmActivateHandlerExpectActivatedAlarm() {
        Alarm alarm = new Alarm();

        assertTrue(alarm.getState() instanceof AlarmDeactivated);

        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new AlarmActivateEventHandler(alarm)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new AlarmEvent(EventType.ALARM_ACTIVATE, "123")
        )));

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.getState() instanceof AlarmActivated);
    }

    @Test
    void testAlarmDeactivateHandlerExpectDeactivatedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.getState() instanceof AlarmActivated);

        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new AlarmDeactivateEventHandler(alarm)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new AlarmEvent(EventType.ALARM_DEACTIVATE, "123")
        )));

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.getState() instanceof AlarmDeactivated);
    }

    @Test
    void testAlarmDeactivateHandlerWithWrongCodeExpectRaisedAlarm() {
        Alarm alarm = new Alarm();

        alarm.activate("123");
        assertTrue(alarm.getState() instanceof AlarmActivated);

        EventProcessor eventProcessor = new HandlingEventProcessor(List.of(new AlarmDeactivateEventHandler(alarm)));

        EventQueue eventQueue = new PredefinedEventQueue(new ArrayDeque<>(List.of(
                new AlarmEvent(EventType.ALARM_DEACTIVATE, "456")
        )));

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();

        assertTrue(alarm.getState() instanceof AlarmRaised);
    }
}
