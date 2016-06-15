package com.eightlight;

/**
 * Created by spatail on 6/15/16.
 */
public class Command {

    public enum CommandType {
        RANGE,
        MIN,
        MAX
    }

    private CommandType type;
    private int min;
    private int max;

    public static  Command createInitCommand(int min, int max) {
        Command command = new Command(CommandType.RANGE);
        command.min = min;
        command.max = max;
        return command;
    }

    public static  Command createMaxCommand(int max) {
        Command command = new Command(CommandType.MAX);
        command.max = max;
        return command;
    }

    public static  Command createMinCommand(int min) {
        Command command = new Command(CommandType.MIN);
        command.min = min;
        return command;
    }

    private Command(CommandType type) {
        this.type = type;
     }

    public CommandType getType() {
        return type;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command = (Command) o;

        if (min != command.min) return false;
        if (max != command.max) return false;
        return type == command.type;

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + min;
        result = 31 * result + max;
        return result;
    }
}
