package lab5;

import java.util.LinkedList;
import java.util.Queue;

public class ActivationQueue {

    private final Queue<MethodRequest> queue = new LinkedList<>();

    public synchronized MethodRequest take() {
        return this.queue.remove();
    }

    public synchronized boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public synchronized void put(MethodRequest req) {
        this.queue.add(req);
    }

    public synchronized int getSize() {
        return this.queue.size();
    }
}
