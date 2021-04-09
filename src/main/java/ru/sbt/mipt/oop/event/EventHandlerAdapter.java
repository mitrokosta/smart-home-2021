package ru.sbt.mipt.oop.event;

import com.coolcompany.smarthome.events.CCSensorEvent;

import java.util.Map;

public class EventHandlerAdapter implements com.coolcompany.smarthome.events.EventHandler {
    private final EventProcessor processor;
    private final Map<String, EventType> eventTranslator;

    public EventHandlerAdapter(EventProcessor processor, Map<String, EventType> translator) {
        this.processor = processor;
        this.eventTranslator = translator;
    }

    @Override
    public void handleEvent(CCSensorEvent event) {
        Event translated = translate(event);
        if (translated != null) {
            processor.processEvent(translated);
        }
    }

    private Event translate(CCSensorEvent event) {
        EventType type = eventTranslator.get(event.getEventType());

        if (type == null) {
            return null;
        }

        return new SensorEvent(type, event.getObjectId());
    }
}
