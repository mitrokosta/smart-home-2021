package ru.sbt.mipt.oop;

public class SmartHomeManager {
    private final SensorEventQueue eventQueue;
    private final SensorEventProcessor sensorEventProcessor;

    public SmartHomeManager(SensorEventQueue eventQueue, SensorEventProcessor sensorEventProcessor) {
        this.eventQueue = eventQueue;
        this.sensorEventProcessor = sensorEventProcessor;
    }

    void processEvents() {
        SensorEvent event = eventQueue.getNextSensorEvent();
        while (event != null) {
            sensorEventProcessor.processEvent(event);
            event = eventQueue.getNextSensorEvent();
        }
    }
}
