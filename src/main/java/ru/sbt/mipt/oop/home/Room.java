package ru.sbt.mipt.oop.home;

import ru.sbt.mipt.oop.action.Action;
import ru.sbt.mipt.oop.action.Actionable;

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

    public String getName() {
        return name;
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
