package jonl.jutils.time;

public class TimeHold {

    private long lastTime;

    public TimeHold() {
        lastTime = System.nanoTime();
    }
    
    public void holdFPS(double fps) {
        long newTime = System.nanoTime();
        long elapsedTime = newTime - lastTime;
        
        
        double length = 1000.0 / fps;
        
        double period = TimeUtils.timeConvert(elapsedTime, TimeMetric.NANO, TimeMetric.MILLI);
        
        if (period < length) {
            double until = length - period;
            try {
                Thread.sleep((long) until);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        lastTime = System.nanoTime();
        
    }
    
}
