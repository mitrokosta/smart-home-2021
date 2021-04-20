package ru.sbt.mipt.oop.rc;

import ru.sbt.mipt.oop.alarm.Alarm;

public class ActivateAlarmCommand implements Command {
    private final Alarm alarm;
    private final String accessCode;

    public ActivateAlarmCommand(Alarm alarm, String accessCode) {
        this.alarm = alarm;
        this.accessCode = accessCode;
    }

    @Override
    public void execute() {
        alarm.activate(accessCode);
    }
}
