package ru.sbt.mipt.oop.alarm;

import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.EventProcessor;

public class AlarmIgnoringProtector implements EventProcessor {
    private final EventProcessor wrapped;
    private final Alarm alarm;

    public AlarmIgnoringProtector(EventProcessor wrapped, Alarm alarm) {
        this.wrapped = wrapped;
        this.alarm = alarm;
    }

    @Override
    public void processEvent(Event event) {
        if (alarm.getState() instanceof AlarmRaised) {
            System.out.println("Sending sms");
        } else {
            wrapped.processEvent(event);
        }
    }
}
