package ru.sbt.mipt.oop.sensor;

import java.util.List;

public class HandlingSensorEventProcessor implements SensorEventProcessor {
    private final List<SensorEventHandler> eventHandlers;

    public HandlingSensorEventProcessor(List<SensorEventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    @Override
    public void processEvent(SensorEvent event) {
        for (SensorEventHandler eventHandler : eventHandlers) {
            eventHandler.handle(event);
        }
    }
}
