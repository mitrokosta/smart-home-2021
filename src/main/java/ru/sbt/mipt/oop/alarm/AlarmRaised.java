package ru.sbt.mipt.oop.alarm;

public class AlarmRaised extends AlarmState {
    public AlarmRaised(Alarm alarm) {
        super(alarm);
    }

    @Override
    public void activate(String accessCode) {

    }

    @Override
    public void deactivate(String accessCode) {
        if (alarm.getAccessCode().equals(accessCode)) {
            alarm.setState(new AlarmDeactivated(alarm));
        }
    }

    @Override
    public void raise() {

    }
}
