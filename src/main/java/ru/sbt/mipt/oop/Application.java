package ru.sbt.mipt.oop;

import java.io.IOException;

import static ru.sbt.mipt.oop.SensorEventType.*;

public class Application {

    public static void main(String... args) throws IOException {
        SmartHome smartHome = new SmartHomeReader(new SmartHomeGsonCreator()).getSmartHome("smart-home-1.js");
        // начинаем цикл обработки событий
        SensorEventQueue eventQueue = new DummySensorEventQueue();
        SensorEvent event = eventQueue.getNextSensorEvent();
        while (event != null) {
            System.out.println("Got event: " + event);
            if (event.getType() == LIGHT_ON || event.getType() == LIGHT_OFF) {
                // событие от источника света
                for (Room room : smartHome.getRooms()) {
                    for (Light light : room.getLights()) {
                        if (light.getId().equals(event.getObjectId())) {
                            if (event.getType() == LIGHT_ON) {
                                light.setOn(true);
                                System.out.println("Light " + light.getId() + " in room " + room.getName() + " was turned on.");
                            } else {
                                light.setOn(false);
                                System.out.println("Light " + light.getId() + " in room " + room.getName() + " was turned off.");
                            }
                        }
                    }
                }
            }
            if (event.getType() == DOOR_OPEN || event.getType() == DOOR_CLOSED) {
                // событие от двери
                for (Room room : smartHome.getRooms()) {
                    for (Door door : room.getDoors()) {
                        if (door.getId().equals(event.getObjectId())) {
                            if (event.getType() == DOOR_OPEN) {
                                door.setOpen(true);
                                System.out.println("Door " + door.getId() + " in room " + room.getName() + " was opened.");
                            } else {
                                door.setOpen(false);
                                System.out.println("Door " + door.getId() + " in room " + room.getName() + " was closed.");
                                // если мы получили событие о закрытие двери в холле - это значит, что была закрыта входная дверь.
                                // в этом случае мы хотим автоматически выключить свет во всем доме (это же умный дом!)
                                if (room.getName().equals("hall")) {
                                    for (Room homeRoom : smartHome.getRooms()) {
                                        for (Light light : homeRoom.getLights()) {
                                            light.setOn(false);
                                            SensorCommand command = new SensorCommand(CommandType.LIGHT_OFF, light.getId());
                                            sendCommand(command);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            event = eventQueue.getNextSensorEvent();
        }
    }

    private static void sendCommand(SensorCommand command) {
        System.out.println("Pretend we're sending command " + command);
    }

}
