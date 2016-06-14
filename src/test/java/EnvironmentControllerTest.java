import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

        assertThat("HVAC should return temperature reading", spy.wasTempTaken(), is(true));
    }

    @Test
    public void shouldReturnTrueIfTemperatureIsIdeal() {
        EnvironmentController controller = new MyEnvironmentController(hvac, 65, 75);

        assertThat("Temperature should be below range", controller.isIdealTemperature(64), is(false));
        assertThat("Temperature should be in range", controller.isIdealTemperature(70), is(true));
        assertThat("Temperature should be above range", controller.isIdealTemperature(76), is(false));
    }

    class HVACSpy implements HVAC {

        private HVAC hvac;
        private boolean tempCalled = false;

        public HVACSpy(HVAC hvac) {
            this.hvac = hvac;
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
            tempCalled = true;
            return hvac.temp();
        }

        public boolean wasTempTaken() {
            return tempCalled;
        }
    }
}
