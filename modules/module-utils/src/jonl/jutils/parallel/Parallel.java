package jonl.jutils.parallel;

import java.util.concurrent.CountDownLatch;

public class Parallel {

    public static Parallel fork(Processor process) {
        Parallel p = new Parallel(process);
        p.run();
        return p;
    }
    
    public static void join(Parallel p) {
        try {
            p.cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    final Processor p;
    CountDownLatch cdl;
    
    Parallel(Processor p) {
        this.p = p;
    }
    
    void run() {
        cdl = new CountDownLatch(1);
        new Thread(new Runnable(){
            @Override
            public void run() {
                p.run();
                cdl.countDown();
            }
        }).start();
        
    }
    
}
