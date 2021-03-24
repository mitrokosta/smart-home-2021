package ru.sbt.mipt.oop.handler;

import ru.sbt.mipt.oop.alarm.Alarm;
import ru.sbt.mipt.oop.event.AlarmEvent;
import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.EventHandler;
import ru.sbt.mipt.oop.event.EventType;

public class AlarmActivateEventHandler implements EventHandler {
    private final Alarm alarm;

    public AlarmActivateEventHandler(Alarm alarm) {
        this.alarm = alarm;
    }

    @Override
    public void handle(Event event) {
        if (event.getType() != EventType.ALARM_ACTIVATE || !(event instanceof AlarmEvent)) {
            return;
        }

        AlarmEvent alarmEvent = (AlarmEvent) event;

        alarm.activate(alarmEvent.getCode());
    }
}
