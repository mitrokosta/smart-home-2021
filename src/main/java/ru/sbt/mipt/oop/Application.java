package ru.sbt.mipt.oop;

import com.coolcompany.smarthome.events.SensorEventsManager;
import ru.sbt.mipt.oop.alarm.*;
import ru.sbt.mipt.oop.command.*;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.input.*;
import ru.sbt.mipt.oop.event.*;
import ru.sbt.mipt.oop.handler.*;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String... args) {
        SmartHomeReader reader = new SmartHomeFileReader("smart-home-1.js", new SmartHomeGsonDeserializer());
        SmartHome smartHome = reader.readSmartHome();

        CommandSender commandSender = new DummyCommandSender();
        Alarm alarm = new Alarm();
        alarm.activate("123"); // демонстрация защиты

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
        eventProcessor = new AlarmIgnoringProtector(eventProcessor, alarm, new SmsIntrusionNotifier());
        eventProcessor = new AlarmIntrusionDetector(eventProcessor, alarm);

        SensorEventsManager manager = new SensorEventsManager();
        manager.registerEventHandler(new EventHandlerAdapter(eventProcessor));
        manager.start();
    }
}
