package ru.sbt.mipt.oop.alarm;


import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.EventProcessor;
import ru.sbt.mipt.oop.event.SensorEvent;

public class AlarmIntrusionDetector implements EventProcessor {
    private final EventProcessor wrapped;
    private final Alarm alarm;

    public AlarmIntrusionDetector(EventProcessor wrapped, Alarm alarm) {
        this.wrapped = wrapped;
        this.alarm = alarm;
    }

    @Override
    public void processEvent(Event event) {
        if (alarm.getState() instanceof AlarmActivated && event instanceof SensorEvent) {
            alarm.raise();
        }

        wrapped.processEvent(event);
    }
}
