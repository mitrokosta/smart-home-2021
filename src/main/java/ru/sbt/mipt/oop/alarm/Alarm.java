package ru.sbt.mipt.oop.alarm;

public class Alarm {
    private AlarmState state;
    private final String accessCode;

    public Alarm(String accessCode) {
        this.state = new AlarmDeactivated(this);
        this.accessCode = accessCode;
    }

    void setState(AlarmState newState) {
        state = newState;
    }

    public AlarmState getState() {
        return state;
    }

    public void activate(String accessCode) {
        if (accessCode.equals(this.accessCode)) {
            state.activate();
        } else {
            state.raise();
        }
    }

    public void deactivate(String accessCode) {
        if (accessCode.equals(this.accessCode)) {
            state.deactivate();
        } else {
            state.raise();
        }
    }

    public void raise() {
        state.raise();
    }
}
