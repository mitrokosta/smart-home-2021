package ru.sbt.mipt.oop.alarm;

public class SmsIntrusionNotifier implements IntrusionNotifier {
    @Override
    public void notifyOwner() {
        System.out.println("Sending sms");
    }
}
