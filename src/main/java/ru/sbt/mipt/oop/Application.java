package ru.sbt.mipt.oop;

import ru.sbt.mipt.oop.input.SmartHomeFileReader;
import ru.sbt.mipt.oop.input.SmartHomeGsonDeserializer;
import ru.sbt.mipt.oop.input.SmartHomeReader;

public class Application {
    public static void main(String... args) {
        SmartHomeReader reader = new SmartHomeFileReader("smart-home-1.js", new SmartHomeGsonDeserializer());
        SmartHome smartHome = reader.getSmartHome();

        SmartHomeManager manager = new SmartHomeManager(new DummySensorEventQueue(),
                new DummySensorEventProcessor(smartHome, new DummyCommandSender()));
        manager.processEvents();
    }
}
