package ru.sbt.mipt.oop.sensor.handler;

import ru.sbt.mipt.oop.alarm.Alarm;
import ru.sbt.mipt.oop.sensor.SensorEvent;
import ru.sbt.mipt.oop.sensor.SensorEventHandler;

public class AlarmActivateSensorEventHandler implements SensorEventHandler {
    private final Alarm alarm;

    public AlarmActivateSensorEventHandler(Alarm alarm) {
        this.alarm = alarm;
    }

    @Override
    public void handle(SensorEvent event) {
        alarm.activate(event.getEventData());
    }
}
