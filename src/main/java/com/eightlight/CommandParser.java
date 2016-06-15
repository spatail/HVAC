package com.eightlight;

/**
 * Created by spatail on 6/15/16.
 */
public class CommandParser {
    public Command parse(String msg) {
        if (msg == null || msg.trim().equals("")) {
            throw new IllegalArgumentException("Message cannot be blank");
        }

        String[] parts = msg.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid command: '" + msg + "'");
        }

        Command.CommandType type = Command.CommandType.valueOf(parts[0].toUpperCase());

        switch (type) {
            case INIT: return handleInitCommand(parts[1]);
            case MIN: return handleSetMinCommand(parts[1]);
            case MAX: return handleSetMaxCommand(parts[1]);
            default:
                throw new IllegalArgumentException("Invalid command: '" + msg + "'");
         }
    }

    private Command handleSetMaxCommand(String part) {
         Integer max = parseInt(part);
         if (max == null) {
            throw new IllegalArgumentException("Invalid Init command: max value cannot be null");
        }
        return Command.createMaxCommand(max);
     }

    private Command handleSetMinCommand(String part) {
        Integer min = parseInt(part);
        if (min == null) {
            throw new IllegalArgumentException("Invalid Init command: min value cannot be null");
        }
        return Command.createMinCommand(min);
     }

    public Command handleInitCommand(String args) {
        String[] parts = args.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Init command argument: '" + args + "'");
        }

        Integer min = parseInt(parts[0]);
        Integer max = parseInt(parts[1]);

        if (min == null) {
            throw new IllegalArgumentException("Invalid Init command: min value cannot be null");
        }
        if (max == null) {
            throw new IllegalArgumentException("Invalid Init command: max value cannot be null");
        }
        return Command.createInitCommand(min, max);
    }

    private Integer parseInt(String intValue) {
        try {
            return Integer.parseInt(intValue);
        } catch (Exception e) {
            return null;
        }
    }
}
