package com.eightlight;

/**
 * Created by spatail on 6/14/16.
 */
public class MyEnvironmentController implements EnvironmentController {

    private HVAC hvac;
    private int minTemp;
    private int maxTemp;
    private LastOn lastOn;
    private LastOn lastOff;
    private int tickCount = 1;

    public MyEnvironmentController(HVAC hvac, int min, int max) {
        this.hvac = hvac;
        this.minTemp = min;
        this.maxTemp = max;
        this.lastOn = null;
        this.lastOff = null;
    }

    @Override
    public void tick() {
        int currTemp = hvac.temp();

        if (currTemp < minTemp) { // Heat
            hvac.heat(true);
            hvac.cool(false);
            toggleFan();
            lastOn = LastOn.Heat;
        } else if (currTemp > maxTemp) { // Cool
            hvac.cool(true);
            hvac.heat(false);
            toggleFan();
            lastOn = LastOn.Cool;
        } else {
            resetTickCount(lastOn);
            lastOff = lastOn;
            hvac.heat(false);
            hvac.fan(false);
            hvac.cool(false);
        }
    }

    @Override
    public void setMax(int max) {
        if (isValidRange(minTemp, max)) {
            this.maxTemp = max;
        }
    }

    @Override
    public int getMax() {
        return maxTemp;
    }

    @Override
    public void setMin(int min) {
        if (isValidRange(min, maxTemp)) {
            this.minTemp = min;
        }
    }

    @Override
    public int getMin() {
        return minTemp;
    }

    private boolean isValidRange(int min, int max) {
        return (max > min) && (max - min) >= 5;
    }

    private void toggleFan() {
        // system just started, or
        // wait elapsed
        if (lastOn == null || tickCount++ > lastOff.wait) {
            hvac.fan(true);
        }
    }

    private void resetTickCount(LastOn lastOn) {
        if (lastOff != lastOn) {
            tickCount = 1;
        }
    }

    private enum LastOn {
        Heat(5), Cool(3);

        final int wait;

        LastOn(int wait) { this.wait = wait; }
    }
}
