package jonl.jutils.parallel;

import java.util.concurrent.CountDownLatch;

public abstract class Task {

    private final CountDownLatch finish = new CountDownLatch(1);
    
    private Report report;
    
    final void setReport(Report report) {
        this.report = report;
    }
    
    final Report getReport() {
        return report;
    }
    
    final void release() {
        finish.countDown();
    }
    
    final void hold() {
        try {
            finish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
