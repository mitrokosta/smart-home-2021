package ru.sbt.mipt.oop.sensor.handler;

import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.home.*;
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

        String targetId = event.getObjectId();
        smartHome.execute((Actionable lightObject) -> {
            if (lightObject instanceof Light) {
                Light light = (Light) lightObject;
                if (light.getId().equals(targetId)) {
                    light.setOn(false);
                }
            }
        });
    }
}
