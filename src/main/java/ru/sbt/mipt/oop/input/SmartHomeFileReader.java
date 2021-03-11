package ru.sbt.mipt.oop.input;

import ru.sbt.mipt.oop.home.SmartHome;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SmartHomeFileReader implements SmartHomeReader {
    private final Path filePath;
    private final SmartHomeDeserializer deserializer;

    public SmartHomeFileReader(String fileName, SmartHomeDeserializer deserializer) {
        this.filePath = Paths.get(fileName);
        this.deserializer = deserializer;
    }

    @Override
    public SmartHome getSmartHome() {
        try {
            return deserializer.deserialize(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
