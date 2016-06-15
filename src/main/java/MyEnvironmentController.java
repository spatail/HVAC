/**
 * Created by spatail on 6/14/16.
 */
public class MyEnvironmentController implements EnvironmentController {

    private HVAC hvac;
    private int minTemp;
    private int maxTemp;
    private LastOn lastOn;
    private LastOn lastOff;
    private int tickCount;

    public MyEnvironmentController(HVAC hvac, int min, int max) {
        this.hvac = hvac;
        this.minTemp = min;
        this.maxTemp = max;
        this.lastOn = null;
        this.lastOff = null;
        resetTickCount();
    }

    @Override
    public void tick() {
        int currTemp = hvac.temp();

        if (isIdealTemperature(currTemp)) {
            lastOff = lastOn;
            hvac.heat(false);
            hvac.fan(false);
            hvac.cool(false);
        } else if (currTemp < minTemp) { // Heat
            hvac.heat(true);
            hvac.cool(false);
            toggleFan();
            lastOn = LastOn.Heat;
        } else if (currTemp > maxTemp) { // Cool
            hvac.cool(true);
            hvac.heat(false);
            toggleFan();
            lastOn = LastOn.Cool;
        }
    }

    @Override
    public boolean isIdealTemperature(int temp) {
        return temp >= minTemp && temp <= maxTemp;
    }

    private void toggleFan() {
        if (lastOn == null) { // system just started
            hvac.fan(true);
        } else if (tickCount++ > lastOff.wait) { // fan wait time elapsed
            hvac.fan(true);
            resetTickCount();
        }
    }

    private void resetTickCount() {
        tickCount = 1;
    }

    private enum LastOn {
        Heat(5), Cool(3);

        final int wait;

        LastOn(int wait) { this.wait = wait; }
    }
}
