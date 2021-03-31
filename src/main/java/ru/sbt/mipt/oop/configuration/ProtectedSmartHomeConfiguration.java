package ru.sbt.mipt.oop.configuration;

import com.coolcompany.smarthome.events.SensorEventsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sbt.mipt.oop.alarm.*;
import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.DummyCommandSender;
import ru.sbt.mipt.oop.event.EventHandler;
import ru.sbt.mipt.oop.event.EventHandlerAdapter;
import ru.sbt.mipt.oop.event.EventProcessor;
import ru.sbt.mipt.oop.event.HandlingEventProcessor;
import ru.sbt.mipt.oop.handler.*;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.input.*;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ProtectedSmartHomeConfiguration {
    @Bean
    SmartHomeReader getSmartHomeReader() {
        return new SmartHomeFileReader(getSmartHomeFileName(), getSmartHomeDeserializer());
    }

    @Bean
    SmartHomeDeserializer getSmartHomeDeserializer() {
        return new SmartHomeGsonDeserializer();
    }

    @Bean
    String getSmartHomeFileName() {
        return "smart-home-1.js";
    }

    @Bean
    SmartHome getSmartHome() {
        return getSmartHomeReader().readSmartHome();
    }

    @Bean
    Alarm getActivatedAlarm() {
        Alarm alarm = new Alarm();
        alarm.activate(getAlarmAccessCode());
        return alarm;
    }

    @Bean
    String getAlarmAccessCode() {
        return "123";
    }

    @Bean
    List<EventHandler> getEventHandlers() {
        SmartHome smartHome = getSmartHome();
        Alarm alarm = getActivatedAlarm();
        CommandSender commandSender = getCommandSender();

        return Arrays.asList(
                new LightOnEventHandler(smartHome),
                new LightOffEventHandler(smartHome),
                new DoorOpenedEventHandler(smartHome),
                new DoorClosedEventHandler(smartHome),
                new HallDoorClosedEventHandler(smartHome, commandSender),
                new AlarmActivateEventHandler(alarm),
                new AlarmDeactivateEventHandler(alarm)
        );
    }

    @Bean
    CommandSender getCommandSender() {
        return new DummyCommandSender();
    }

    @Bean
    IntrusionNotifier getIntrusionNotifier() {
        return new SmsIntrusionNotifier();
    }

    @Bean
    EventProcessor getProtectedEventProcessor() {
        Alarm alarm = getActivatedAlarm();
        IntrusionNotifier intrusionNotifier = getIntrusionNotifier();

        EventProcessor eventProcessor = new HandlingEventProcessor(getEventHandlers());
        eventProcessor = new AlarmIgnoringProtector(eventProcessor, alarm, intrusionNotifier);
        eventProcessor = new AlarmIntrusionDetector(eventProcessor, alarm);

        return eventProcessor;
    }

    @Bean
    com.coolcompany.smarthome.events.EventHandler getSensorEventHandler() {
        return new EventHandlerAdapter(getProtectedEventProcessor());
    }

    @Bean
    SensorEventsManager getSensorEventsManager() {
        SensorEventsManager sensorEventsManager = new SensorEventsManager();
        sensorEventsManager.registerEventHandler(getSensorEventHandler());
        return sensorEventsManager;
    }
}
