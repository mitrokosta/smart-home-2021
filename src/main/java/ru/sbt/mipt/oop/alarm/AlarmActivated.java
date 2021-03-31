package ru.sbt.mipt.oop.alarm;

public class AlarmActivated extends AlarmState {
    public AlarmActivated(Alarm alarm) {
        super(alarm);
    }

    @Override
    public void activate(String accessCode) {

    }

    @Override
    public void deactivate(String accessCode) {
        if (alarm.getAccessCode().equals(accessCode)) {
            alarm.setState(new AlarmDeactivated(alarm));
        } else {
            alarm.setState(new AlarmRaised(alarm));
        }
    }

    @Override
    public void raise() {
        alarm.setState(new AlarmRaised(alarm));
    }
}
