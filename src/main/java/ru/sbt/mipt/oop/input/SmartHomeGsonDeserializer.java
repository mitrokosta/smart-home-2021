package ru.sbt.mipt.oop.input;

import com.google.gson.Gson;
import ru.sbt.mipt.oop.SmartHome;

public class SmartHomeGsonDeserializer implements SmartHomeDeserializer {
    @Override
    public SmartHome deserialize(byte[] serialized) {
        return new Gson().fromJson(new String(serialized), SmartHome.class);
    }
}
