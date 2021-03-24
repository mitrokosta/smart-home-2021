package ru.sbt.mipt.oop.handler;

import ru.sbt.mipt.oop.event.Event;
import ru.sbt.mipt.oop.event.EventHandler;
import ru.sbt.mipt.oop.util.Counter;

public class CountingEventHandler implements EventHandler {
    private final Counter counter;

    public CountingEventHandler(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void handle(Event event) {
        counter.add();
    }
}
