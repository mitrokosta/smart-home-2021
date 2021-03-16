package ru.sbt.mipt.oop.home;

import java.util.Collection;

public class Room implements Actionable {
    private final Collection<Light> lights;
    private final Collection<Door> doors;
    private final String name;

    public Room(Collection<Light> lights, Collection<Door> doors, String name) {
        this.lights = lights;
        this.doors = doors;
        this.name = name;
    }

    public Collection<Light> getLights() {
        return lights;
    }

    public Collection<Door> getDoors() {
        return doors;
    }

    public String getName() {
        return name;
    }

    public boolean hasDoor(String doorId) {
        for (Door door : doors) {
            if (door.getId().equals(doorId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute(Action action) {
        for (Actionable component : lights) {
            component.execute(action);
        }

        for (Actionable component : doors) {
            component.execute(action);
        }

        action.accept(this);
    }
}
