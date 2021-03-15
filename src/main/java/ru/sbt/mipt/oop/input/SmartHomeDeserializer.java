package ru.sbt.mipt.oop.input;

import ru.sbt.mipt.oop.home.SmartHome;

public interface SmartHomeDeserializer {
    SmartHome deserialize(byte[] serialized);
}
