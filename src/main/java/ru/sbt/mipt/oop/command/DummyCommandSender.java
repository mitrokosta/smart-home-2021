package ru.sbt.mipt.oop.command;

import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.SensorCommand;

public class DummyCommandSender implements CommandSender {
    @Override
    public void sendCommand(SensorCommand command) {
        System.out.println("Pretend we're sending command " + command);
    }
}
