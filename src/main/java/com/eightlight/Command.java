package com.eightlight;

/**
 * Created by spatail on 6/15/16.
 */
public class Command {

    public enum CommandType {
        INIT
    }

    private CommandType type;
    private int min;
    private int max;

    public Command(CommandType type, int min, int max) {
        this.type = type;
        this.min = min;
        this.max = max;
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
