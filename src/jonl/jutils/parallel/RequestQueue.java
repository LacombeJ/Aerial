package jonl.jutils.parallel;

import java.util.ArrayDeque;

import jonl.jutils.func.Function;
import jonl.jutils.func.Tuple2;

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
    
    public Tuple2<X,Y> respond() {
        X request = dequeueRequest();
        Y response = null;
        if (request!=null) {
            response = responder.f(request);
            request.setResponse(response);
            request.release();
        }
        return new Tuple2<>(request,response);
    }
    
    private synchronized X dequeueRequest() {
        return queue.pollFirst();
    }
    
    private synchronized void queueRequest(X request) {
        queue.addLast(request);;
    }
    


    
    
    
}
