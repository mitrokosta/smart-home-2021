package ru.sbt.mipt.oop.event.handler;

import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.SensorEvent;
import ru.sbt.mipt.oop.home.*;
import ru.sbt.mipt.oop.event.EventHandler;
import ru.sbt.mipt.oop.event.EventType;

public class LightOffEventHandler implements EventHandler {
    private final SmartHome smartHome;

    public LightOffEventHandler(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void handle(Event event) {
        if (event.getType() != EventType.LIGHT_OFF || !(event instanceof SensorEvent)) {
            return;
        }

        SensorEvent sensorEvent = (SensorEvent) event;

        String targetId = sensorEvent.getObjectId();
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
