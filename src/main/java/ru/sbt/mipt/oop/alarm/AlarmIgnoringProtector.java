package ru.sbt.mipt.oop.alarm;

import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.EventProcessor;

public class AlarmIgnoringProtector implements EventProcessor {
    private final EventProcessor wrapped;
    private final Alarm alarm;
    private final IntrusionNotifier notifier;

    public AlarmIgnoringProtector(EventProcessor wrapped, Alarm alarm, IntrusionNotifier notifier) {
        this.wrapped = wrapped;
        this.alarm = alarm;
        this.notifier = notifier;
    }

    @Override
    public void processEvent(Event event) {
        if (alarm.isRaised()) {
            notifier.notifyOwner();
        } else {
            wrapped.processEvent(event);
        }
    }
}
