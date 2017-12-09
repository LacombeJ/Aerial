package jonl.jutils.parallel;

/**
 * Runs the processes in sequential order on a separate thread
 * 
 * @author Jonathan Lacombe
 *
 */
public class SequentialProcessor extends MultiProcessor {
    
    @Override
    public void runProcess() {
        final SequentialProcessor sp = this;
        new Thread(new Runnable(){
            @Override
            public void run() {
                for (int i=0; i<sp.getProcessorCount(); i++) {
                    Processor p = sp.getProcessor(i);
                    p.run();
                }
            }
        }).start();
    }

}
