package ru.sbt.mipt.oop.home.util;

import ru.sbt.mipt.oop.home.Light;
import ru.sbt.mipt.oop.home.Room;

public class LightSearchQuery {
    private final Light light;
    private final Room room;

    public LightSearchQuery(Light light, Room room) {
        this.light = light;
        this.room = room;
    }

    public Light getLight() {
        return light;
    }

    public Room getRoom() {
        return room;
    }
}
