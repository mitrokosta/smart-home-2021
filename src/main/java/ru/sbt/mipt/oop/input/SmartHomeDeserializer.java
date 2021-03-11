package ru.sbt.mipt.oop.input;

import ru.sbt.mipt.oop.SmartHome;

public interface SmartHomeDeserializer {
    SmartHome deserialize(byte[] serialized);
}
