package ru.sbt.mipt.oop;

public class DummySensorEventProcessor implements SensorEventProcessor {
    private final SmartHome smartHome;
    private final CommandSender commandSender;

    public DummySensorEventProcessor(SmartHome smartHome, CommandSender commandSender) {
        this.smartHome = smartHome;
        this.commandSender = commandSender;
    }

    @Override
    public void processEvent(SensorEvent event) {
        System.out.println("Got event: " + event);
        switch (event.getType()) {
            case LIGHT_ON:
                processLightOn(event);
                break;

            case LIGHT_OFF:
                processLightOff(event);
                break;

            case DOOR_OPEN:
                processDoorOpen(event);
                break;

            case DOOR_CLOSED:
                processDoorClosed(event);
                break;
        }
    }

    private void processLightOn(SensorEvent event) {
        for (Room room : smartHome.getRooms()) {
            for (Light light : room.getLights()) {
                if (light.getId().equals(event.getObjectId())) {
                    light.setOn(true);
                    System.out.println("Light " + light.getId() + " in room " + room.getName() + " was turned on.");
                }
            }
        }
    }

    private void processLightOff(SensorEvent event) {
        for (Room room : smartHome.getRooms()) {
            for (Light light : room.getLights()) {
                if (light.getId().equals(event.getObjectId())) {
                    light.setOn(false);
                    System.out.println("Light " + light.getId() + " in room " + room.getName() + " was turned off.");
                }
            }
        }
    }

    private void processDoorOpen(SensorEvent event) {
        for (Room room : smartHome.getRooms()) {
            for (Door door : room.getDoors()) {
                if (door.getId().equals(event.getObjectId())) {
                    door.setOpen(true);
                    System.out.println("Door " + door.getId() + " in room " + room.getName() + " was opened.");
                }
            }
        }
    }

    private void processDoorClosed(SensorEvent event) {
        for (Room room : smartHome.getRooms()) {
            for (Door door : room.getDoors()) {
                if (door.getId().equals(event.getObjectId())) {
                    door.setOpen(false);
                    System.out.println("Door " + door.getId() + " in room " + room.getName() + " was closed.");
                    // если мы получили событие о закрытие двери в холле - это значит, что была закрыта входная дверь.
                    // в этом случае мы хотим автоматически выключить свет во всем доме (это же умный дом!)
                    if (room.getName().equals("hall")) {
                        for (Room homeRoom : smartHome.getRooms()) {
                            for (Light light : homeRoom.getLights()) {
                                light.setOn(false);
                                SensorCommand command = new SensorCommand(CommandType.LIGHT_OFF, light.getId());
                                commandSender.sendCommand(command);
                            }
                        }
                    }
                }
            }
        }
    }
}
