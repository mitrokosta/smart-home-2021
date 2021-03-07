package ru.sbt.mipt.oop;

import java.io.IOException;

public class Application {
    public static void main(String... args) throws IOException {
        SmartHome smartHome = new SmartHomeReader(new SmartHomeGsonCreator()).getSmartHome("smart-home-1.js");
        SmartHomeManager manager = new SmartHomeManager(new DummySensorEventQueue(),
                new DummySensorEventProcessor(smartHome, new DummyCommandSender()));
        manager.processEvents();
    }
}
