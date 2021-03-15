package ru.sbt.mipt.oop.sensor;

public class SensorEventLoop {
    private final SensorEventQueue eventQueue;
    private final SensorEventProcessor sensorEventProcessor;

    public SensorEventLoop(SensorEventQueue eventQueue, SensorEventProcessor sensorEventProcessor) {
        this.eventQueue = eventQueue;
        this.sensorEventProcessor = sensorEventProcessor;
    }

    public void start() {
        SensorEvent event = eventQueue.getNextSensorEvent();
        while (event != null) {
            sensorEventProcessor.processEvent(event);
            event = eventQueue.getNextSensorEvent();
        }
    }
}
