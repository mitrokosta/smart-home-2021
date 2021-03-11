package ru.sbt.mipt.oop.sensor.handler;

import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.home.util.LightSearchQuery;
import ru.sbt.mipt.oop.home.util.SmartHomeUtils;
import ru.sbt.mipt.oop.sensor.SensorEvent;
import ru.sbt.mipt.oop.sensor.SensorEventHandler;
import ru.sbt.mipt.oop.sensor.SensorEventType;

public class LightOffSensorEventHandler implements SensorEventHandler {
    private final SmartHome smartHome;

    public LightOffSensorEventHandler(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void handle(SensorEvent event) {
        if (event.getType() != SensorEventType.LIGHT_OFF) {
            return;
        }

        LightSearchQuery searchQuery = SmartHomeUtils.findLight(smartHome, event.getObjectId());
        if (searchQuery != null) {
            Light light = searchQuery.getLight();
            Room room = searchQuery.getRoom();

            light.setOn(false);
            System.out.println("Light " + light.getId() + " in room " + room.getName() + " was turned off.");
        }
    }
}
