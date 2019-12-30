package rma.ox.data.bdd;

import com.badlogic.gdx.ai.utils.CircularBuffer;

import rma.ox.engine.core.threading.AbsPausableThread;

public class NoSqlThread extends AbsPausableThread {

    private CircularBuffer<NoSqlRequest> requestQueue;
    private NoSqlRequest currentRequest;

    public NoSqlThread(){
        requestQueue = new CircularBuffer<>(10);
        thread.start();
    }

    public void addRequest(NoSqlRequest request){
        requestQueue.store(request);
        resume();
    }

    @Override
    public void runThread() {
        if (currentRequest == null) currentRequest = requestQueue.read();

        while (currentRequest != null) {

            boolean finished = currentRequest.request();

            if (!finished) return;

            // Read next request from the queue
            currentRequest = requestQueue.read();
        }
    }
}
