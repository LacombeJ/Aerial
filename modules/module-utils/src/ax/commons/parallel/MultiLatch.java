package ax.commons.parallel;

import java.util.concurrent.CountDownLatch;

public class MultiLatch {
    
    private CountDownLatch cdl;
    
    public MultiLatch(int count) {
        setCount(count);
    }
    
    public MultiLatch() {
        this(1);
    }
    
    public synchronized void setCount(int numLocks) {
        if (cdl!=null) {
            while (cdl.getCount()!=0) {
                cdl.countDown();
            }
        }
        cdl = new CountDownLatch(numLocks);
    }
    
    /** Used for debugging and testing purposes */
    public synchronized long getCount() {
        return cdl.getCount();
    }
    
    public synchronized void countDown() {
        cdl.countDown();
    }
    
    public void hold() {
        CountDownLatch latch;
        synchronized(this) {
            latch = cdl;
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
