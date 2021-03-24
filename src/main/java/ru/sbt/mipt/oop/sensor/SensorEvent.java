package ru.sbt.mipt.oop.sensor;

public class SensorEvent {
    private final SensorEventType type;
    private final String eventData;

    public SensorEvent(SensorEventType type, String eventData) {
        this.type = type;
        this.eventData = eventData;
    }

    public SensorEventType getType() {
        return type;
    }

    public String getEventData() {
        return eventData;
    }

    @Override
    public String toString() {
        return "SensorEvent{" +
                "type=" + type +
                ", eventData='" + eventData + '\'' +
                '}';
    }
}
