package ru.sbt.mipt.oop.alarm;

public class AlarmActivated extends AlarmState {
    public AlarmActivated(Alarm alarm) {
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
        alarm.setState(new AlarmRaised(alarm));
    }
}
