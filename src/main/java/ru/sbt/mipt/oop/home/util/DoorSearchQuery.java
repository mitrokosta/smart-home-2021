package ru.sbt.mipt.oop.home.util;

import ru.sbt.mipt.oop.home.Door;
import ru.sbt.mipt.oop.home.Room;

public class DoorSearchQuery {
    private final Door door;
    private final Room room;

    public DoorSearchQuery(Door door, Room room) {
        this.door = door;
        this.room = room;
    }

    public Door getDoor() {
        return door;
    }

    public Room getRoom() {
        return room;
    }
}
