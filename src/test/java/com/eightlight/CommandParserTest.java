package com.eightlight;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by spatail on 6/15/16.
 */
public class CommandParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void itShouldFailOnNullMessage() {
        String msg = null;
        CommandParser parser = new CommandParser();
        Command command = parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldFailOnEmptyMessage() {
        String msg = "";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldFailWhenCommandHas1Argument() {
        String msg = "type";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldFailWhenCommandHas3Arguments() {
        String msg = "type:type:type";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldFailWhenCommandTypeIsInvalid() {
        String msg = "type:value";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itFailsForInitCommandWith1Value() {
        String msg = "init:65";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itFailsForInitCommandWith3Values() {
        String msg = "init:65,70,75";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itFailsForInitCommandWithStringMinValue() {
        String msg = "init:sixty,70";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itFailsForInitCommandWithStringMaxValue() {
        String msg = "init:60,seventy";
        CommandParser parser = new CommandParser();
        parser.parse(msg);
    }

    @Test
    public void itParsesInitCommand() {
        String msg = "init:65,75";
        CommandParser parser = new CommandParser();
        Command result = parser.parse(msg);

        Command expected = new Command(Command.CommandType.INIT, 65, 75);

        assertEquals("Should have parsed Init command", result, expected);
    }
}
