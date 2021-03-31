package ru.sbt.mipt.oop.event;

public abstract class Event {
    protected final EventType type;

    protected Event(EventType type) {
        this.type = type;
    }

    public final EventType getType() {
        return type;
    }
}
