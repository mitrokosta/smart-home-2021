package ru.sbt.mipt.oop.event;

public class AlarmEvent extends Event {
    private final String code;

    public AlarmEvent(EventType type, String code) {
        super(type);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
