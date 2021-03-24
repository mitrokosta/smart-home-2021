package ru.sbt.mipt.oop.event.handler;

import ru.sbt.mipt.oop.action.Action;
import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.CommandType;
import ru.sbt.mipt.oop.command.SensorCommand;
import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.SensorEvent;
import ru.sbt.mipt.oop.home.*;
import ru.sbt.mipt.oop.event.EventHandler;
import ru.sbt.mipt.oop.event.EventType;

public class HallDoorClosedEventHandler implements EventHandler {
    private final SmartHome smartHome;
    private final CommandSender commandSender;

    public HallDoorClosedEventHandler(SmartHome smartHome, CommandSender commandSender) {
        this.smartHome = smartHome;
        this.commandSender = commandSender;
    }

    @Override
    public void handle(Event event) {
        if (event.getType() != EventType.DOOR_CLOSED || !(event instanceof SensorEvent)) {
            return;
        }

        SensorEvent sensorEvent = (SensorEvent) event;

        Action turnEveryLightOff = (Actionable lightObject) -> {
            if (lightObject instanceof Light) {
                Light light = (Light) lightObject;
                light.setOn(false);
                SensorCommand command = new SensorCommand(CommandType.LIGHT_OFF, light.getId());
                commandSender.sendCommand(command);
            }
        };

        String targetId = sensorEvent.getObjectId();

        Action checkHallDoor = (Actionable doorObject) -> {
            if (doorObject instanceof Door && ((Door) doorObject).getId().equals(targetId)) {
                smartHome.execute(turnEveryLightOff);
            }
        };

        smartHome.execute((Actionable roomObject) -> {
            if (roomObject instanceof Room) {
                Room room = (Room) roomObject;
                // если мы холл и нашу дверь закрыли, то...
                if ("hall".equals(room.getName())) {
                    room.execute(checkHallDoor);
                }
            }
        });
    }
}
