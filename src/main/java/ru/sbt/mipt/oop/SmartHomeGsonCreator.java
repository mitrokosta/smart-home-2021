package ru.sbt.mipt.oop;

import com.google.gson.Gson;

public class SmartHomeGsonCreator implements SmartHomeCreator {
    @Override
    public SmartHome createSmartHome(String serialized) {
        return new Gson().fromJson(serialized, SmartHome.class);
    }
}
