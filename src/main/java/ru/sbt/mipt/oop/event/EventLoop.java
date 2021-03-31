package ru.sbt.mipt.oop.event;

public class EventLoop {
    private final EventQueue eventQueue;
    private final EventProcessor eventProcessor;

    public EventLoop(EventQueue eventQueue, EventProcessor eventProcessor) {
        this.eventQueue = eventQueue;
        this.eventProcessor = eventProcessor;
    }

    public void start() {
        Event event = eventQueue.getNextEvent();
        while (event != null) {
            eventProcessor.processEvent(event);
            event = eventQueue.getNextEvent();
        }
    }
}
