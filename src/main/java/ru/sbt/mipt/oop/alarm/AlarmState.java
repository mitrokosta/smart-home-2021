package ru.sbt.mipt.oop.alarm;

public abstract class AlarmState {
    protected Alarm alarm;

    public AlarmState(Alarm alarm) {
        this.alarm = alarm;
    }

    public abstract void activate(String accessCode);
    public abstract void deactivate(String accessCode);
    public abstract void raise();
}
