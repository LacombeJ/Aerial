package ax.commons.parallel;

import java.util.concurrent.CountDownLatch;

public abstract class Request {

    private final CountDownLatch finish = new CountDownLatch(1);
    
    private Response response;
    
    final void setResponse(Response response) {
        this.response = response;
    }
    
    final Response getResponse() {
        return response;
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
