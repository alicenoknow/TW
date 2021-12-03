package lab5;

import lab5.utils.TaskType;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class Scheduler implements Runnable {
    private final Queue<MethodRequest> primaryQueue = new LinkedList<>();
    private TaskType currentPrimaryTask = TaskType.TAKE;

    public final SynchronousQueue<MethodRequest> entryQueue = new SynchronousQueue<>();

    public static int totalConsumed = 0;
    public static int totalProduced = 0;

    @Override
    public void run() {
        while (true) {
            if (!this.primaryQueue.isEmpty()) {
                MethodRequest primaryReq = this.primaryQueue.peek();
                if (primaryReq.guard()) {
                    primaryReq.call();
                    this.primaryQueue.remove();
                    continue;
                }
            }

            MethodRequest entryReq = null;
            try {
                entryReq = this.entryQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(this.primaryQueue.isEmpty()) {
                if (entryReq.guard()) {
                    entryReq.call();
                } else {
                    this.primaryQueue.add(entryReq);
                    this.currentPrimaryTask = entryReq.getTaskType();
                }
            } else if (entryReq.getTaskType() != this.currentPrimaryTask) {
                entryReq.call();
            } else {
                this.primaryQueue.add(entryReq);
            }
        }
    }

}
