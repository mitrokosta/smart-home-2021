package ru.sbt.mipt.oop.home.util;

import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.Room;
import ru.sbt.mipt.oop.home.SmartHome;

public abstract class SmartHomeUtils {
    public static LightSearchQuery findLight(SmartHome smartHome, String lightId) {
        for (Room room : smartHome.getRooms()) {
            for (Light light : room.getLights()) {
                if (light.getId().equals(lightId)) {
                    return new LightSearchQuery(light, room);
                }
            }
        }
        return null;
    }

    public static DoorSearchQuery findDoor(SmartHome smartHome, String doorId) {
        for (Room room : smartHome.getRooms()) {
            for (Door door : room.getDoors()) {
                if (door.getId().equals(doorId)) {
                    return new DoorSearchQuery(door, room);
                }
            }
        }
        return null;
    }
}
