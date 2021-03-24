package ru.sbt.mipt.oop.alarm;

public abstract class AlarmState {
    protected Alarm alarm;

    public AlarmState(Alarm alarm) {
        this.alarm = alarm;
    }

    public abstract void activate();
    public abstract void deactivate();
    public abstract void raise();
}
