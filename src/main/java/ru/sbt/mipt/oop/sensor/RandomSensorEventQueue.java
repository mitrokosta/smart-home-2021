package ru.sbt.mipt.oop.sensor;

import ru.sbt.mipt.oop.sensor.SensorEvent;
import ru.sbt.mipt.oop.sensor.SensorEventQueue;
import ru.sbt.mipt.oop.sensor.SensorEventType;

public class RandomSensorEventQueue implements SensorEventQueue {
    @Override
    public SensorEvent getNextSensorEvent() {
        // pretend like we're getting the events from physical world, but here we're going to just generate some random events
        if (Math.random() < 0.05) return null; // null means end of event stream
        SensorEventType sensorEventType = SensorEventType.values()[(int) (4 * Math.random())];
        String objectId = "" + ((int) (10 * Math.random()));
        return new SensorEvent(sensorEventType, objectId);
    }
}
