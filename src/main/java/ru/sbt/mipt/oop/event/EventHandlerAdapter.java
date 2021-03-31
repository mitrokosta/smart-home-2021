package ru.sbt.mipt.oop.event;

import com.coolcompany.smarthome.events.CCSensorEvent;

public class EventHandlerAdapter implements com.coolcompany.smarthome.events.EventHandler {
    private final EventProcessor processor;

    public EventHandlerAdapter(EventProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void handleEvent(CCSensorEvent event) {
        Event translated = translate(event);
        if (translated != null) {
            processor.processEvent(translated);
        }
    }

    private Event translate(CCSensorEvent event) {
        EventType type;

        switch (event.getEventType()) {
            case "LightIsOn":
                type = EventType.LIGHT_ON;
                break;

            case "LightIsOff":
                type = EventType.LIGHT_OFF;
                break;

            case "DoorIsOpen":
                type = EventType.DOOR_OPEN;
                break;

            case "DoorIsClosed":
                type = EventType.DOOR_CLOSED;
                break;

            default:
                return null;
        }

        return new SensorEvent(type, event.getObjectId());
    }
}
