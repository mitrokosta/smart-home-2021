package ru.sbt.mipt.oop;

import com.google.gson.Gson;

public class SmartHomeGsonDeserializer implements SmartHomeDeserializer {
    @Override
    public SmartHome deserialize(byte[] serialized) {
        return new Gson().fromJson(new String(serialized), SmartHome.class);
    }
}
