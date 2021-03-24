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
        alarm.setState(new AlarmDeactivated(alarm));
    }

    @Override
    public void raise() {

    }
}
