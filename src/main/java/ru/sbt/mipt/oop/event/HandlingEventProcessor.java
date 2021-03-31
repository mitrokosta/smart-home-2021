package ru.sbt.mipt.oop.event;

import java.util.List;

public class HandlingEventProcessor implements EventProcessor {
    private final List<EventHandler> eventHandlers;

    public HandlingEventProcessor(List<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    @Override
    public void processEvent(Event event) {
        for (EventHandler eventHandler : eventHandlers) {
            eventHandler.handle(event);
        }
    }
}
