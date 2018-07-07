package ax.commons.parallel;

import java.util.ArrayDeque;

import ax.commons.func.Function;

public class TaskQueue<X extends Task, Y extends Report> {

    private final ArrayDeque<X> queue = new ArrayDeque<>();
    private final Function<X,Y> reporter;
    
    public TaskQueue(Function<X,Y> reporter) {
        this.reporter = reporter;
    }
    
    public void task(X task) {
        queueTask(task);
    }
    
    public void report() {
        X task = dequeueTask();
        if (task!=null) {
            Y report = reporter.f(task);
            task.setReport(report);
            task.release();
        }
    }
    
    @SuppressWarnings("unchecked")
    public Y hold(X task) {
        task.hold();
        Y report = (Y) task.getReport();
        return report;
    }
    
    private synchronized X dequeueTask() {
        return queue.pollFirst();
    }
    
    private synchronized void queueTask(X Task) {
        queue.addLast(Task);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
    


    
    
    
}
