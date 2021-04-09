package ru.sbt.mipt.oop.configuration;

import com.coolcompany.smarthome.events.SensorEventsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sbt.mipt.oop.alarm.*;
import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.DummyCommandSender;
import ru.sbt.mipt.oop.event.*;
import ru.sbt.mipt.oop.handler.*;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.input.SmartHomeDeserializer;
import ru.sbt.mipt.oop.input.SmartHomeFileReader;
import ru.sbt.mipt.oop.input.SmartHomeGsonDeserializer;
import ru.sbt.mipt.oop.input.SmartHomeReader;

import java.util.ArrayList;
import java.util.Collection;
import static java.util.Map.entry;
import java.util.Map;

@Configuration
public class ProtectedSmartHomeConfiguration {
    @Bean
    SmartHomeReader smartHomeReader(String smartHomeFileName, SmartHomeDeserializer smartHomeDeserializer) {
        return new SmartHomeFileReader(smartHomeFileName, smartHomeDeserializer);
    }

    @Bean
    SmartHomeDeserializer smartHomeDeserializer() {
        return new SmartHomeGsonDeserializer();
    }

    @Bean
    String smartHomeFileName() {
        return "smart-home-1.js";
    }

    @Bean
    SmartHome smartHome(SmartHomeReader smartHomeReader) {
        return smartHomeReader.readSmartHome();
    }

    @Bean
    Alarm activatedAlarm(String alarmAccessCode) {
        Alarm alarm = new Alarm();
        alarm.activate(alarmAccessCode);
        return alarm;
    }

    @Bean
    String alarmAccessCode() {
        return "123";
    }

    @Bean
    EventHandler lightOnEventHandler(SmartHome smartHome) {
        return new LightOnEventHandler(smartHome);
    }

    @Bean
    EventHandler lightOffEventHandler(SmartHome smartHome) {
        return new LightOffEventHandler(smartHome);
    }

    @Bean
    EventHandler doorOpenedEventHandler(SmartHome smartHome) {
        return new DoorOpenedEventHandler(smartHome);
    }

    @Bean
    EventHandler doorClosedEventHandler(SmartHome smartHome) {
        return new DoorClosedEventHandler(smartHome);
    }

    @Bean
    EventHandler hallDoorClosedEventHandler(SmartHome smartHome, CommandSender commandSender) {
        return new HallDoorClosedEventHandler(smartHome, commandSender);
    }

    @Bean
    EventHandler alarmActivateEventHandler(Alarm activatedAlarm) {
        return new AlarmActivateEventHandler(activatedAlarm);
    }

    @Bean
    EventHandler alarmDeactivateEventHandler(Alarm activatedAlarm) {
        return new AlarmDeactivateEventHandler(activatedAlarm);
    }

    @Bean
    CommandSender commandSender() {
        return new DummyCommandSender();
    }

    @Bean
    IntrusionNotifier intrusionNotifier() {
        return new SmsIntrusionNotifier();
    }

    @Bean
    EventProcessor eventProcessor(Collection<EventHandler> eventHandlers) {
        return new HandlingEventProcessor(new ArrayList<>(eventHandlers));
    }

    @Bean
    EventProcessor protectedEventProcessor(EventProcessor eventProcessor, Alarm activatedAlarm, IntrusionNotifier intrusionNotifier) {
        eventProcessor = new AlarmIgnoringProtector(eventProcessor, activatedAlarm, intrusionNotifier);
        eventProcessor = new AlarmIntrusionDetector(eventProcessor, activatedAlarm);
        return eventProcessor;
    }

    @Bean
    Map<String, EventType> eventTranslator() {
        return Map.ofEntries(
                entry("LightIsOn", EventType.LIGHT_ON),
                entry("LightIsOff", EventType.LIGHT_OFF),
                entry("DoorIsOpen", EventType.DOOR_OPEN),
                entry("DoorIsClosed", EventType.DOOR_CLOSED)
        );
    }

    @Bean
    com.coolcompany.smarthome.events.EventHandler adaptedSensorEventHandler(EventProcessor protectedEventProcessor, Map<String, EventType> eventTranslator) {
        return new EventHandlerAdapter(protectedEventProcessor, eventTranslator);
    }

    @Bean
    SensorEventsManager sensorEventsManager(com.coolcompany.smarthome.events.EventHandler adaptedSensorEventHandler) {
        SensorEventsManager sensorEventsManager = new SensorEventsManager();
        sensorEventsManager.registerEventHandler(adaptedSensorEventHandler);
        return sensorEventsManager;
    }
}
