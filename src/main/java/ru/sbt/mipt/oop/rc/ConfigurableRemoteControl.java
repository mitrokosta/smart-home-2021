package ru.sbt.mipt.oop.rc;

import rc.RemoteControl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigurableRemoteControl implements RemoteControl {
    private final Map<String, Command> controls;

    // Создать пульт с заданным набором кнопок и действием по умолчанию
    public ConfigurableRemoteControl(Collection<String> buttons, Command defaultCommand) {
        controls = new HashMap<>();
        buttons.forEach(button -> controls.put(button, defaultCommand));
    }

    public ConfigurableRemoteControl(Collection<String> buttons) {
        this(buttons, () -> {}); // ничего не делающие изначально кнопки
    }

    public void setCommandForButton(String button, Command command) {
        if (command != null) {
            controls.replace(button, command); // replace т.к. у пульта кнопки фиксированны
        }
    }

    @Override
    public void onButtonPressed(String buttonCode) {
        Command command = controls.get(buttonCode);
        if (command != null) {
            command.execute();
        }
    }
}
