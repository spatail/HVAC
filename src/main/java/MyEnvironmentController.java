/**
 * Created by spatail on 6/14/16.
 */
public class MyEnvironmentController implements EnvironmentController {

    private HVAC hvac;
    private int defaultMin = 65;
    private int defaultMax = 75;
    private LastOn lastOn = null;
    private LastOn lastoff = null;
    private int tickCount = 1;

    public MyEnvironmentController(HVAC hvac) {
        this.hvac = hvac;
    }

    public MyEnvironmentController(HVAC hvac, int min, int max) {
        this.hvac = hvac;
        this.defaultMin = min;
        this.defaultMax = max;
    }

    @Override
    public void tick() {
        int currTemp = hvac.temp();

        if (isIdealTemperature(currTemp)) {
            lastoff = lastOn;
            hvac.heat(false);
            hvac.fan(false);
            hvac.cool(false);
        } else if (currTemp < defaultMin) { // Heat
            hvac.heat(true);
            hvac.cool(false);
            toggleFan();
            lastOn = LastOn.Heat;
        } else if (currTemp > defaultMax) { // Cool
            hvac.cool(true);
            hvac.heat(false);
            toggleFan();
            lastOn = LastOn.Cool;
        }
    }

    @Override
    public boolean isIdealTemperature(int temp) {
        return temp >= defaultMin && temp <= defaultMax;
    }

    private void toggleFan() {
        if (lastOn == null) {
            hvac.fan(true);
        } else if (tickCount++ > lastoff.wait) {
            hvac.fan(true);
        }
    }

    enum LastOn {
        Heat(5), Cool(3);

        int wait;

        LastOn(int wait) { this.wait = wait; }

        public int getWait() { return wait; }
    }
}
