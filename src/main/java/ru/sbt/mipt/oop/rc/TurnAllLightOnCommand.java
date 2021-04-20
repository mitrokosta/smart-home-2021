package ru.sbt.mipt.oop.rc;

import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.SmartHome;

public class TurnAllLightOnCommand implements Command {
    private final SmartHome smartHome;

    public TurnAllLightOnCommand(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void execute() {
        smartHome.execute((Actionable lightObject) -> {
            if (lightObject instanceof Light) {
                ((Light) lightObject).setOn(true);
            }
        });
    }
}
