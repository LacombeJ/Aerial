package ax.commons.parallel;

import java.util.concurrent.CountDownLatch;

/**
 * Runs processes in parallel on the thread where run() is caled
 * @author Jonathan Lacombe
 *
 */
public final class ParallelProcessor extends MultiProcessor {
    
    @Override
    public void runProcess() {
        final ParallelProcessor pp = this;
        int numProcesses = pp.getProcessorCount();
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(numProcesses);
        for (int i=0; i<numProcesses; i++) {
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        start.await();
                        pp.getProcessor(j).run();
                        finish.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        start.countDown();
        try {
            finish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
}



