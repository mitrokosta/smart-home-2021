package ru.sbt.mipt.oop.rc;

import ru.sbt.mipt.oop.action.Action;
import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;

public class CloseHallDoorCommand implements Command {
    private final SmartHome smartHome;

    public CloseHallDoorCommand(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void execute() {
        Action closeDoors = (Actionable doorObject) -> {
            if (doorObject instanceof Door) {
                ((Door) doorObject).setOpen(false);
            }
        };

        smartHome.execute((Actionable roomObject) -> {
            if (roomObject instanceof Room) {
                Room room = (Room) roomObject;

                if ("hall".equals(room.getName())) {
                    room.execute(closeDoors);
                }
            }
        });
    }
}
