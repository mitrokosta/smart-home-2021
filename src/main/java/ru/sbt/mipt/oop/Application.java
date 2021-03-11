package ru.sbt.mipt.oop;

import ru.sbt.mipt.oop.command.DummyCommandSender;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.input.SmartHomeFileReader;
import ru.sbt.mipt.oop.input.SmartHomeGsonDeserializer;
import ru.sbt.mipt.oop.input.SmartHomeReader;
import ru.sbt.mipt.oop.sensor.RandomSensorEventQueue;
import ru.sbt.mipt.oop.sensor.SensorEventLoop;
import ru.sbt.mipt.oop.sensor.SensorEventProcessorImpl;

public class Application {
    public static void main(String... args) {
        SmartHomeReader reader = new SmartHomeFileReader("smart-home-1.js", new SmartHomeGsonDeserializer());
        SmartHome smartHome = reader.getSmartHome();

        SensorEventLoop eventLoop = new SensorEventLoop(new RandomSensorEventQueue(),
                new SensorEventProcessorImpl(smartHome, new DummyCommandSender()));
        eventLoop.start();
    }
}
