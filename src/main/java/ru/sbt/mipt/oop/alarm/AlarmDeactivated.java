package ru.sbt.mipt.oop.alarm;

public class AlarmDeactivated extends AlarmState {
    public AlarmDeactivated(Alarm alarm) {
        super(alarm);
    }

    @Override
    public void activate() {
        alarm.setState(new AlarmActivated(alarm));
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void raise() {

    }
}
