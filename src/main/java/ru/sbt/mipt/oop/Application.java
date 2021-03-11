package ru.sbt.mipt.oop;

import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.DummyCommandSender;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.input.SmartHomeFileReader;
import ru.sbt.mipt.oop.input.SmartHomeGsonDeserializer;
import ru.sbt.mipt.oop.input.SmartHomeReader;
import ru.sbt.mipt.oop.sensor.*;
import ru.sbt.mipt.oop.sensor.handler.*;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String... args) {
        SmartHomeReader reader = new SmartHomeFileReader("smart-home-1.js", new SmartHomeGsonDeserializer());
        SmartHome smartHome = reader.getSmartHome();

        CommandSender commandSender = new DummyCommandSender();
        SensorEventQueue eventQueue = new RandomSensorEventQueue();

        List<SensorEventHandler> handlers = Arrays.asList(
                new LightOnSensorEventHandler(smartHome),
                new LightOffSensorEventHandler(smartHome),
                new DoorOpenedSensorEventHandler(smartHome),
                new DoorClosedSensorEventHandler(smartHome),
                new HallDoorClosedSensorEventHandler(smartHome, commandSender)
        );

        SensorEventProcessor sensorEventProcessor = new HandlingSensorEventProcessor(handlers);

        SensorEventLoop eventLoop = new SensorEventLoop(eventQueue, sensorEventProcessor);
        eventLoop.start();
    }
}
