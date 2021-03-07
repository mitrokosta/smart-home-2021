package ru.sbt.mipt.oop;

public class SmartHomeManager {
    private final SensorEventQueue eventQueue;
    private final EventsProcessor eventsProcessor;

    public SmartHomeManager(SensorEventQueue eventQueue, EventsProcessor eventsProcessor) {
        this.eventQueue = eventQueue;
        this.eventsProcessor = eventsProcessor;
    }

    void processEvents() {
        SensorEvent event = eventQueue.getNextSensorEvent();
        while (event != null) {
            eventsProcessor.processEvent(event);
            event = eventQueue.getNextSensorEvent();
        }
    }
}
