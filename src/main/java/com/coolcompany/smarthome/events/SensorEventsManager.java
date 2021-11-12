package com.coolcompany.smarthome.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class SensorEventsManager {
    private final String[] eventTypes = new String[] { "LightIsOn", "LightIsOff", "DoorIsOpen", "DoorIsClosed", "DoorIsLocked", "DoorIsUnlocked" };

    private Collection<EventHandler> handlers = new ArrayList<>();

    private final Random random = new Random();

    public void registerEventHandler(EventHandler handler) {
        this.handlers.add(handler);
    }

    public void start() {
        CCSensorEvent event = getNextSensorEvent();
        while (event != null) {
            for (EventHandler handler : handlers) {
                handler.handleEvent(event);
            }
            event = getNextSensorEvent();
        }
    }

    private CCSensorEvent getNextSensorEvent() {
        // pretend like we're getting the events from physical world, but here we're going to just generate some random events
        if (Math.random() < 0.05) return null; // null means end of event stream
        String sensorEventType = eventTypes[random.nextInt(6)];
        String objectId = Integer.toString(random.nextInt(10));
        return new CCSensorEvent(sensorEventType, objectId);
    }


}
