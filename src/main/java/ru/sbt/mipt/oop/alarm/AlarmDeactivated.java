package ru.sbt.mipt.oop.alarm;

public class AlarmDeactivated extends AlarmState {
    public AlarmDeactivated(Alarm alarm) {
        super(alarm);
    }

    @Override
    public void activate(String accessCode) {
        alarm.setAccessCode(accessCode);
        alarm.setState(new AlarmActivated(alarm));
    }

    @Override
    public void deactivate(String accessCode) {
        // do nothing
    }

    @Override
    public void raise() {
        alarm.setState(new AlarmRaised(alarm));
    }
}
