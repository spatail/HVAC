package com.eightlight;

/**
 * Created by spatail on 6/15/16.
 */
public class SystemManager {
    private final CommandParser parser;
    private final EnvironmentController controller;

    public SystemManager(CommandParser parser, EnvironmentController controller) {
        this.parser = parser;
        this.controller = controller;
    }

    public void handleMessage(String msg) {
        Command command = parser.parse(msg);

        switch (command.getType()) {
            case RANGE: controller.setRange(command.getMin(), command.getMax()); break;
            case MIN: controller.setMin(command.getMin()); break;
            case MAX: controller.setMax(command.getMax()); break;
            default: throw new IllegalArgumentException("Should not happen");
        }
    }
}
