package ax.commons.parallel;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Class with multiple processors
 * 
 * @author Jonathan Lacombe
 *
 */
public abstract class MultiProcessor implements Processor {

    private final ArrayList<Processor> processes = new ArrayList<>();
    private CountDownLatch cdl;
    private Status status = new Status();
    private int lockCount = 0;
    
    /** Starts the processor threads */
    @Override
    public final void run() {
        cdl = new CountDownLatch(lockCount);
        runProcess();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /** Do not call this method - this method is called by MultiProcessor.start() */
    protected abstract void runProcess();
    
    /**
     * Sets the lock count for a CountDownLatch. After run() is called, the thread
     * will wait for this processors count down to be equal to 0 before continuing.
     * This value is initially 0.
     * See {@link #countDown()}.
     * @param n
     */
    public final void setLockCount(int n) {
        lockCount = n;
    }
    
    /**
     * Decrements the lock count down by 1. When the lockCount==0, the thread that
     * called run() will continue.
     */
    public final void countDown() {
        lockCount--;
        if (cdl!=null) {
            cdl.countDown();
        }
    }
    
    public final Status getStatus() {
        return status;
    }
    
    public final void add(Processor process) {
        processes.add(process);
    }
    
    protected final int getProcessorCount() {
        return processes.size();
    }
    
    protected final Processor getProcessor(int i) {
        return processes.get(i);
    }
    
}
