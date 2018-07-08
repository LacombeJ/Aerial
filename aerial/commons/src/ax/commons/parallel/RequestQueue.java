package ax.commons.parallel;

import java.util.ArrayDeque;

import ax.commons.func.Function;

public class RequestQueue<X extends Request, Y extends Response> {

    private final ArrayDeque<X> queue = new ArrayDeque<>();
    private final Function<X,Y> responder;
    
    public RequestQueue(Function<X,Y> responder) {
        this.responder = responder;
    }
    
    @SuppressWarnings("unchecked")
    public Y request(X request) {
        queueRequest(request);
        request.hold();
        Y response = (Y) request.getResponse();
        return response;
    }
    
    public void respond() {
        X request = dequeueRequest();
        if (request!=null) {
            Y response = responder.f(request);
            request.setResponse(response);
            request.release();
        }
    }
    
    private synchronized X dequeueRequest() {
        return queue.pollFirst();
    }
    
    private synchronized void queueRequest(X request) {
        queue.addLast(request);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
    


    
    
    
}
