/**
 * Created by spatail on 6/14/16.
 */
public class MyEnvironmentController implements EnvironmentController {

    private HVAC hvac;
    private int defaultMin = 65;
    private int defaultMax = 75;

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
        isIdealTemperature(hvac.temp());
    }

    @Override
    public boolean isIdealTemperature(int temp) {
        return temp >= defaultMin && temp <= defaultMax;
    }
}