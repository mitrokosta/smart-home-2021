package ru.sbt.mipt.oop.rc;

import ru.sbt.mipt.oop.alarm.Alarm;

public class RaiseAlarmCommand implements Command {
    private final Alarm alarm;

    public RaiseAlarmCommand(Alarm alarm) {
        this.alarm = alarm;
    }

    @Override
    public void execute() {
        alarm.raise();
    }
}
