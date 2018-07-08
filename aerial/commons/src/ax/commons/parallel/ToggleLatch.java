package ax.commons.parallel;

import java.util.concurrent.CountDownLatch;

/**
 * This latch will pause the current thread on hold() if toggled on
 * <p>
 * Default setting is toggled on
 * <p>
 * When toggleOff() is called, hold() will release the current thread
 * <p>
 * This is a CountDownLatch where count down 1=toggled on and 0=toggled off
 * @author Jonathan Lacombe
 *
 */
public class ToggleLatch {

    private CountDownLatch cdl = new CountDownLatch(1);
    
    public boolean isToggleOn() {
        return cdl.getCount()==1;
    }
    
    public void toggleOff() {
        cdl.countDown();
    }
    
    /**
     * Toggles off then toggles back on
     * <p>
     * This method togglesOff to release any thread
     * currently waiting on the latch
     */
    public void toggleOn() {
        toggleOff();
        cdl = new CountDownLatch(1);
    }
    
    /**
     * Holds this thread until toggleOff() is called
     */
    public void hold() {
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
