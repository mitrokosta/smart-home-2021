package ru.sbt.mipt.oop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SmartHomeReader {
    private final SmartHomeCreator creator;

    public SmartHomeReader(SmartHomeCreator creator) {
        this.creator = creator;
    }

    public SmartHome getSmartHome(String fileName) throws IOException {
        return creator.createSmartHome(new String(Files.readAllBytes(Paths.get(fileName))));
    }
}
