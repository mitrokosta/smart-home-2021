package ru.sbt.mipt.oop.sensor.handler;

import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.home.util.DoorSearchQuery;
import ru.sbt.mipt.oop.home.util.SmartHomeUtils;
import ru.sbt.mipt.oop.sensor.SensorEvent;
import ru.sbt.mipt.oop.sensor.SensorEventHandler;
import ru.sbt.mipt.oop.sensor.SensorEventType;

public class DoorClosedSensorEventHandler implements SensorEventHandler {
    private final SmartHome smartHome;

    public DoorClosedSensorEventHandler(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void handle(SensorEvent event) {
        if (event.getType() != SensorEventType.DOOR_CLOSED) {
            return;
        }

        DoorSearchQuery searchQuery = SmartHomeUtils.findDoor(smartHome, event.getObjectId());
        if (searchQuery != null) {
            Door door = searchQuery.getDoor();
            Room room = searchQuery.getRoom();

            door.setOpen(false);
            System.out.println("Door " + door.getId() + " in room " + room.getName() + " was closed.");
        }
    }
}
