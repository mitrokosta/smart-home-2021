package ru.sbt.mipt.oop;

import ru.sbt.mipt.oop.alarm.Alarm;
import ru.sbt.mipt.oop.alarm.AlarmIgnoringProtector;
import ru.sbt.mipt.oop.alarm.AlarmIntrusionDetector;
import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.DummyCommandSender;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.input.SmartHomeFileReader;
import ru.sbt.mipt.oop.input.SmartHomeGsonDeserializer;
import ru.sbt.mipt.oop.input.SmartHomeReader;
import ru.sbt.mipt.oop.event.*;
import ru.sbt.mipt.oop.handler.*;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String... args) {
        SmartHomeReader reader = new SmartHomeFileReader("smart-home-1.js", new SmartHomeGsonDeserializer());
        SmartHome smartHome = reader.readSmartHome();

        CommandSender commandSender = new DummyCommandSender();
        EventQueue eventQueue = new RandomEventQueue();
        Alarm alarm = new Alarm("123");

        List<EventHandler> handlers = Arrays.asList(
                new LightOnEventHandler(smartHome),
                new LightOffEventHandler(smartHome),
                new DoorOpenedEventHandler(smartHome),
                new DoorClosedEventHandler(smartHome),
                new HallDoorClosedEventHandler(smartHome, commandSender),
                new AlarmActivateEventHandler(alarm),
                new AlarmDeactivateEventHandler(alarm)
        );

        EventProcessor eventProcessor = new HandlingEventProcessor(handlers);
        eventProcessor = new AlarmIgnoringProtector(eventProcessor, alarm);
        eventProcessor = new AlarmIntrusionDetector(eventProcessor, alarm);

        EventLoop eventLoop = new EventLoop(eventQueue, eventProcessor);
        eventLoop.start();
    }
}
