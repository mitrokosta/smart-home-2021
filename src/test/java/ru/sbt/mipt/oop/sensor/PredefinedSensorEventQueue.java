package ru.sbt.mipt.oop.sensor;

import java.util.Queue;

public class PredefinedSensorEventQueue implements SensorEventQueue {
    private final Queue<SensorEvent> queue;

    public PredefinedSensorEventQueue(Queue<SensorEvent> queue) {
        this.queue = queue;
    }

    @Override
    public SensorEvent getNextSensorEvent() {
        if (queue.isEmpty()) {
            return null;
        }

        return queue.remove();
    }
}
