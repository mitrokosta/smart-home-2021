package ru.sbt.mipt.oop.sensor.handler;

import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.sensor.SensorEvent;
import ru.sbt.mipt.oop.sensor.SensorEventHandler;
import ru.sbt.mipt.oop.sensor.SensorEventType;

public class DoorOpenedSensorEventHandler implements SensorEventHandler {
    private final SmartHome smartHome;

    public DoorOpenedSensorEventHandler(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void handle(SensorEvent event) {
        if (event.getType() != SensorEventType.DOOR_OPEN) {
            return;
        }

        String targetId = event.getObjectId();
        smartHome.execute((Actionable doorObject) -> {
            if (doorObject instanceof Door) {
                Door door = (Door) doorObject;
                if (door.getId().equals(targetId)) {
                    door.setOpen(true);
                }
            }
        });
    }
}
