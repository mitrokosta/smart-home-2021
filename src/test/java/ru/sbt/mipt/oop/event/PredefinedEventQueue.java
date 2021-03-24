package ru.sbt.mipt.oop.event;

import java.util.Queue;

public class PredefinedEventQueue implements EventQueue {
    private final Queue<SensorEvent> queue;

    public PredefinedEventQueue(Queue<SensorEvent> queue) {
        this.queue = queue;
    }

    @Override
    public Event getNextEvent() {
        if (queue.isEmpty()) {
            return null;
        }

        return queue.remove();
    }
}
