package ru.sbt.mipt.oop.alarm;

public class AlarmRaised extends AlarmState {
    public AlarmRaised(Alarm alarm) {
        super(alarm);
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {
        // не позволяем себя выключить, если поднята тревога
    }

    @Override
    public void raise() {

    }
}
