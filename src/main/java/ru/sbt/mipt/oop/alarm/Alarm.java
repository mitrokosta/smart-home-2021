package ru.sbt.mipt.oop.alarm;

public class Alarm {
    private AlarmState state = new AlarmDeactivated(this);
    private String accessCode;

    void setState(AlarmState newState) {
        state = newState;
    }

    public AlarmState getState() {
        return state;
    }

    void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    String getAccessCode() {
        return accessCode;
    }

    public void activate(String accessCode) {
        state.activate(accessCode);
    }

    public void deactivate(String accessCode) {
        state.deactivate(accessCode);
    }

    public void raise() {
        state.raise();
    }
}
