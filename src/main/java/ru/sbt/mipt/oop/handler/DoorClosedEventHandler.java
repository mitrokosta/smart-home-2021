package ru.sbt.mipt.oop.handler;

import ru.sbt.mipt.oop.action.Actionable;
import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.SensorEvent;
import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.SmartHome;
import ru.sbt.mipt.oop.event.EventHandler;
import ru.sbt.mipt.oop.event.EventType;

public class DoorClosedEventHandler implements EventHandler {
    private final SmartHome smartHome;

    public DoorClosedEventHandler(SmartHome smartHome) {
        this.smartHome = smartHome;
    }

    @Override
    public void handle(Event event) {
        if (event.getType() != EventType.DOOR_CLOSED || !(event instanceof SensorEvent)) {
            return;
        }

        SensorEvent sensorEvent = (SensorEvent) event;

        String targetId = sensorEvent.getObjectId();
        smartHome.execute((Actionable doorObject) -> {
            if (doorObject instanceof Door) {
                Door door = (Door) doorObject;
                if (door.getId().equals(targetId)) {
                    door.setOpen(false);
                }
            }
        });
    }
}
