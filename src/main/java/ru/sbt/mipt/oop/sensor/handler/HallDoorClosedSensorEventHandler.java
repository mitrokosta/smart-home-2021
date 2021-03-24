package ru.sbt.mipt.oop.sensor.handler;

import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.CommandType;
import ru.sbt.mipt.oop.command.SensorCommand;
import ru.sbt.mipt.oop.home.*;
import ru.sbt.mipt.oop.sensor.SensorEvent;
import ru.sbt.mipt.oop.sensor.SensorEventHandler;
import ru.sbt.mipt.oop.sensor.SensorEventType;

public class HallDoorClosedSensorEventHandler implements SensorEventHandler {
    private final SmartHome smartHome;
    private final CommandSender commandSender;

    public HallDoorClosedSensorEventHandler(SmartHome smartHome, CommandSender commandSender) {
        this.smartHome = smartHome;
        this.commandSender = commandSender;
    }

    @Override
    public void handle(SensorEvent event) {
        if (event.getType() != SensorEventType.DOOR_CLOSED) {
            return;
        }

        Action turnEveryLightOff = (Actionable lightObject) -> {
            if (lightObject instanceof Light) {
                Light light = (Light) lightObject;
                light.setOn(false);
                SensorCommand command = new SensorCommand(CommandType.LIGHT_OFF, light.getId());
                commandSender.sendCommand(command);
            }
        };

        String targetId = event.getEventData();

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
