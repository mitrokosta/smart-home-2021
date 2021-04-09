package ru.sbt.mipt.oop.rc;

import ru.sbt.mipt.oop.action.Action;
import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;

public class TurnHallDoorLightOnCommand implements Command {
    private final SmartHome smartHome;

    public TurnHallDoorLightOnCommand(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void execute() {
        Action turnLightOn = (Actionable lightObject) -> {
            if (lightObject instanceof Light) {
                ((Light) lightObject).setOn(true);
            }
        };

        smartHome.execute((Actionable roomObject) -> {
            if (roomObject instanceof Room) {
                Room room = (Room) roomObject;

                if ("hall".equals(room.getName())) {
                    room.execute(turnLightOn);
                }
            }
        });
    }
}
