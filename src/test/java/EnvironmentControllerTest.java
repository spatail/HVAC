import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by spatail on 6/14/16.
 */
public class EnvironmentControllerTest {

    HVAC hvac;

    @Before
    public void setup() {
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
                return 10;
            }
        };
    }


    @Test
    public void shouldGetTemperatureFromHVAC() {
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy);
        controller.tick();

        assertThat("HVAC should return temperature reading", spy.tempCalled, is(true));
    }

    @Test
    public void shouldReturnTrueIfTemperatureIsIdeal() {
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        assertThat("Temperature should be below range", controller.isIdealTemperature(64), is(false));
        assertThat("Temperature should be in range", controller.isIdealTemperature(65), is(true));
        assertThat("Temperature should be in range", controller.isIdealTemperature(70), is(true));
        assertThat("Temperature should be in range", controller.isIdealTemperature(75), is(true));
        assertThat("Temperature should be above range", controller.isIdealTemperature(76), is(false));
    }

    @Test
    public void shouldTurnEverythingOffWhenTemperatureIsIdeal() {
        HVAC hvac = createHVAC(70);
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();

        assertThat("Heat should be off", spy.heatStatus, is(false));
        assertThat("Cool should be off", spy.coolStatus, is(false));
        assertThat("Fan should be off", spy.fanStatus, is(false));
    }

    @Test
    public void shouldTurnHeatOnWhenTemperatureIsBelowMin() {
        HVAC hvac = createHVAC(64);
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();

        assertThat("Heat should be on", spy.heatStatus, is(true));
        assertThat("Cool should be off", spy.coolStatus, is(false));
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldTurnCoolOnWhenTemperatureIsAboveMax() {
        HVAC hvac = createHVAC(76);
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();

        assertThat("Heat should be off", spy.heatStatus, is(false));
        assertThat("Cool should be on", spy.coolStatus, is(true));
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldNotRunFanFor3TicksAfterCoolIsOff() {
        HVAC hvac = new HVACWithVariableTemperatures(new int[] {76, 75, 76, 76, 76, 76});
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldNotRunFanFor5TicksAfterHeatIsOff() {
        HVAC hvac = new HVACWithVariableTemperatures(new int[] {64, 65, 64, 64, 64, 64, 64, 64});
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldNotRunFanFor5TicksAfterHeatIsOffAndCoolIsOn() {
        HVAC hvac = new HVACWithVariableTemperatures(new int[] {64, 65, 76, 76, 76, 76, 76, 76});
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldNotRunFanFor3TicksAfterCoolIsOffAndHeatIsOn() {
        HVAC hvac = new HVACWithVariableTemperatures(new int[] {76, 75, 64, 64, 64, 64});
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick();
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    class HVACSpy implements HVAC {

        private HVAC hvac;

        boolean tempCalled = false;
        boolean heatStatus = false;
        boolean coolStatus = false;
        boolean fanStatus = false;

        public HVACSpy(HVAC hvac) {
            this.hvac = hvac;
        }

        @Override
        public void heat(boolean on) {
            heatStatus = on;
        }

        @Override
        public void cool(boolean on) {
            coolStatus = on;
        }

        @Override
        public void fan(boolean on) {
            fanStatus = on;
        }

        @Override
        public int temp() {
            tempCalled = true;
            return hvac.temp();
        }
    }

    private HVAC createHVAC(final int temp) {
        return new HVAC() {
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
                return temp;
            }
        };
    }

    class HVACWithVariableTemperatures implements HVAC {

        private int[] temps;
        private int index = 0;

        public HVACWithVariableTemperatures(int[] temps) {
            this.temps = temps;
        }

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
            return temps[index++];
        }
    }
}
