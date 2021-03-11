package ru.sbt.mipt.oop;

public interface SmartHomeDeserializer {
    SmartHome deserialize(byte[] serialized);
}
