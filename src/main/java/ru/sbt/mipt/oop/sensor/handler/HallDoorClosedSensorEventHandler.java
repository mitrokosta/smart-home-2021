package ru.sbt.mipt.oop.sensor.handler;

import ru.sbt.mipt.oop.command.CommandSender;
import ru.sbt.mipt.oop.command.CommandType;
import ru.sbt.mipt.oop.command.SensorCommand;
import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.home.util.DoorSearchQuery;
import ru.sbt.mipt.oop.home.util.SmartHomeUtils;
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

        DoorSearchQuery searchQuery = SmartHomeUtils.findDoor(smartHome, event.getObjectId());
        if (searchQuery != null) {
            Room room = searchQuery.getRoom();
            // если мы получили событие о закрытие двери в холле - это значит, что была закрыта входная дверь.
            // в этом случае мы хотим автоматически выключить свет во всем доме (это же умный дом!)
            if (room.getName().equals("hall")) {
                for (Room homeRoom : smartHome.getRooms()) {
                    for (Light light : homeRoom.getLights()) {
                        light.setOn(false);
                        SensorCommand command = new SensorCommand(CommandType.LIGHT_OFF, light.getId());
                        commandSender.sendCommand(command);
                    }
                }
            }
        }
    }
}
