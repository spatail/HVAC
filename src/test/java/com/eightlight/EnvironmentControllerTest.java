package com.eightlight;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by spatail on 6/14/16.
 */
public class EnvironmentControllerTest {

    @Test
    public void shouldTurnEverythingOffWhenTemperatureIsIdeal() {
        HVAC hvac = createHVACWithTemp(70);
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();

        assertThat("Heat called", spy.heatCalled, is(true));
        assertThat("Cool called", spy.coolCalled, is(true));
        assertThat("Fan called", spy.fanCalled, is(true));

        assertThat("Heat should be off", spy.heatStatus, is(false));
        assertThat("Cool should be off", spy.coolStatus, is(false));
        assertThat("Fan should be off", spy.fanStatus, is(false));
    }

    @Test
    public void shouldTurnHeatOnWhenTemperatureIsBelowMin() {
        HVAC hvac = createHVACWithTemp(64);
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();

        assertThat("Heat called", spy.heatCalled, is(true));
        assertThat("Cool called", spy.coolCalled, is(true));
        assertThat("Fan called", spy.fanCalled, is(true));

        assertThat("Heat should be on", spy.heatStatus, is(true));
        assertThat("Cool should be off", spy.coolStatus, is(false));
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldTurnCoolOnWhenTemperatureIsAboveMax() {
        HVAC hvac = createHVACWithTemp(76);
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick();

        assertThat("Heat called", spy.heatCalled, is(true));
        assertThat("Cool called", spy.coolCalled, is(true));
        assertThat("Fan called", spy.fanCalled, is(true));

        assertThat("Heat should be off", spy.heatStatus, is(false));
        assertThat("Cool should be on", spy.coolStatus, is(true));
        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldNotRunFanFor3TicksAfterCoolIsOff() {
        HVAC hvac = createHVACWithVariableTemperatures(new int[] {76, 75, 76, 76, 76, 76});
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
        HVAC hvac = createHVACWithVariableTemperatures(new int[] {64, 65, 64, 64, 64, 64, 64, 64});
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
        HVAC hvac = createHVACWithVariableTemperatures(new int[] {64, 65, 76, 76, 76, 76, 76, 76});
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
        HVAC hvac = createHVACWithVariableTemperatures(new int[] {76, 75, 64, 64, 64, 64});
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
    public void shouldResetWaitPeriodAfterFanGoesOn() {
        HVAC hvac = createHVACWithVariableTemperatures(new int[] {76, 75, 64, 64, 64, 64, 75, 64, 64, 64, 64, 64, 64});
        HVACSpy spy = new HVACSpy(hvac);

        EnvironmentController controller = new MyEnvironmentController(spy, 65, 75);

        controller.tick(); // on
        controller.tick(); // off, reset
        controller.tick(); // on
        controller.tick(); // on
        controller.tick(); // on
        controller.tick(); // on

        controller.tick(); // off

        assertThat("Fan should be off", spy.fanStatus, is(false));

        controller.tick(); // on
        controller.tick(); // on

        assertThat("Fan should be on", spy.fanStatus, is(false));

        controller.tick(); // on
        controller.tick(); // on
        controller.tick(); // on
        controller.tick(); // on

        assertThat("Fan should be on", spy.fanStatus, is(true));
    }

    @Test
    public void shouldIncreaseMaxTemperature() {
        HVAC hvac = createHVACWithTemp(70);
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        controller.setMax(80);

        assertThat(controller.getMax(), is(80));
    }

    @Test
    public void shouldDecreaseMinTemperature() {
        HVAC hvac = createHVACWithTemp(70);
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        controller.setMin(60);

        assertThat(controller.getMin(), is(60));
    }

    @Test
    public void shouldNotChangeMMaxTempWhenMaxIsLowerThanMin() {
        HVAC hvac = createHVACWithTemp(70);
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        controller.setMax(64);

        assertThat(controller.getMin(), is(65));
        assertThat(controller.getMax(), is(75));
    }

    @Test
    public void shouldNotChangeMinTempWhenMinIsHigherThanMax() {
        HVAC hvac = createHVACWithTemp(70);
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        controller.setMin(76);

        assertThat(controller.getMin(), is(65));
        assertThat(controller.getMax(), is(75));
    }

    @Test
    public void rangeDifferenceShouldBeAtleast5WhenVaryingMin() {
        HVAC hvac = createHVACWithTemp(70);
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        controller.setMin(72);

        assertThat(controller.getMin(), is(65));
        assertThat(controller.getMax(), is(75));

        controller.setMin(70);

        assertThat(controller.getMin(), is(70));
        assertThat(controller.getMax(), is(75));

        controller.setMin(69);

        assertThat(controller.getMin(), is(69));
        assertThat(controller.getMax(), is(75));
    }

    @Test
    public void rangeDifferenceShouldBeAtleast5WhenVaryingMax() {
        HVAC hvac = createHVACWithTemp(70);
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        controller.setMax(69);

        assertThat(controller.getMin(), is(65));
        assertThat(controller.getMax(), is(75));

        controller.setMax(70);

        assertThat(controller.getMin(), is(65));
        assertThat(controller.getMax(), is(70));

        controller.setMax(71);

        assertThat(controller.getMin(), is(65));
        assertThat(controller.getMax(), is(71));
    }

    class HVACSpy implements HVAC {

        private HVAC hvac;

        boolean tempCalled = false;
        boolean heatStatus = false;
        boolean coolStatus = false;
        boolean fanStatus = false;

        boolean heatCalled = false;
        boolean coolCalled = false;
        boolean fanCalled = false;

        public HVACSpy(HVAC hvac) {
            this.hvac = hvac;
        }

        @Override
        public void heat(boolean on) {
            heatCalled = true;
            heatStatus = on;
        }

        @Override
        public void cool(boolean on) {
            coolCalled = true;
            coolStatus = on;
        }

        @Override
        public void fan(boolean on) {
            fanCalled = true;
            fanStatus = on;
        }

        @Override
        public int temp() {
            tempCalled = true;
            return hvac.temp();
        }
    }

    private HVAC createHVACWithTemp(final int temp) {
        return new HVACAdatper() {
            @Override
            public int temp() {
                return temp;
            }
        };
    }

    private HVAC createHVACWithVariableTemperatures(int[] temps) {
        return new HVACWithVariableTemperatures(temps);
    }

    class HVACWithVariableTemperatures extends HVACAdatper {

        private int[] temps;
        private int index = 0;

        public HVACWithVariableTemperatures(int[] temps) {
            this.temps = temps;
        }

        @Override
        public int temp() {
            return temps[index++];
        }
    }

    abstract class HVACAdatper implements HVAC {

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
    }
}
