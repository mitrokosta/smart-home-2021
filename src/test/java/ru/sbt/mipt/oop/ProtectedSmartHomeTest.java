package ru.sbt.mipt.oop;

import com.coolcompany.smarthome.events.SensorEventsManager;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import ru.sbt.mipt.oop.configuration.ProtectedSmartHomeConfiguration;

public class ProtectedSmartHomeTest {
    @Test
    void testApp() {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(ProtectedSmartHomeConfiguration.class);
        SensorEventsManager sensorEventsManager = context.getBean(SensorEventsManager.class);
        sensorEventsManager.start();
    }
}
