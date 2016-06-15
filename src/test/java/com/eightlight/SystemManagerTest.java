package com.eightlight;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by spatail on 6/15/16.
 */
public class SystemManagerTest {

    CommandParser parser;
    HVAC hvac;
    EnvironmentController controller;

    @Before
    public void setup() {
        parser = new CommandParser();
        hvac = new HVAC() {
            @Override
            public void heat(boolean on) {

            }

            @Override
            public void cool(boolean on) {

            }

            @Override
            public void fan(boolean on) {

            }

            @Override
            public int temp() {
                return 0;
            }
        };

        controller = new MyEnvironmentController(hvac, 60, 70);
    }

    @Test
    public void itShouldSetTemperatureRangeFromInitMessage() {
        String msg = "range:80,90";
        SystemManager manager = new SystemManager(parser, controller);
        manager.handleMessage(msg);

        assertThat(controller.getMin(), is(80));
        assertThat(controller.getMax(), is(90));
    }

    @Test
    public void itShouldSetMinTemperatureRangeFromMinMessage() {
        String msg = "min:64";
        SystemManager manager = new SystemManager(parser, controller);
        manager.handleMessage(msg);

        assertThat(controller.getMin(), is(64));
        assertThat(controller.getMax(), is(70));
    }

    @Test
    public void itShouldSetMaxTemperatureRangeFromMaxMessage() {
        String msg = "max:92";
        SystemManager manager = new SystemManager(parser, controller);
        manager.handleMessage(msg);

        assertThat(controller.getMin(), is(60));
        assertThat(controller.getMax(), is(92));
    }
}
