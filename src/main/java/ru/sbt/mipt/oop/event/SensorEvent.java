package ru.sbt.mipt.oop.event;

public class SensorEvent extends Event {
    private final String objectId;

    public SensorEvent(EventType type, String objectId) {
        super(type);
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return "SensorEvent{" +
                "type=" + type +
                ", objectId='" + objectId + '\'' +
                '}';
    }
}
