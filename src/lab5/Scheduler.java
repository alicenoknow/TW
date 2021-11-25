package lab5;

import lab5.utils.TaskType;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class Scheduler implements Runnable {
    private final Queue<MethodRequest> primaryQueue = new LinkedList<>();
    private TaskType currentPrimaryTask = TaskType.TAKE;

    public final ActivationQueue entryQueue = new ActivationQueue();

    public static int totalConsumed = 0;
    public static int totalProduced = 0;

    @Override
    public void run() {
        while (true) {
            if (!this.primaryQueue.isEmpty()) {
                MethodRequest primaryReq = this.primaryQueue.peek();
                if (primaryReq.guard()) {
                    primaryReq.call();
                    System.out.println("Primary task done " + primaryReq.getTaskType());
                    this.primaryQueue.remove();
                    continue;
                }
            }

            System.out.println("Cannot take from primary, goes to entry");
            while(this.entryQueue.isEmpty()){};
            MethodRequest entryReq = this.entryQueue.take();
            System.out.println("Took from entry " + entryReq.getTaskType());

            if(this.primaryQueue.isEmpty()) {
                if (entryReq.guard()) {
                    entryReq.call();
                    System.out.println("Entry task done " + entryReq.getTaskType());
                    System.out.println("Primary ");
                    printPrimaryQ();
                    System.out.println("Entry ");
                    printEntry();
                } else {
                    System.out.println("Put on primary");
                    this.primaryQueue.add(entryReq);
                    this.currentPrimaryTask = entryReq.getTaskType();
                }
            } else if (entryReq.getTaskType() != this.currentPrimaryTask) {
                entryReq.call();
                System.out.println("Entry task done "  + entryReq.getTaskType());
                System.out.println("Primary ");
                printPrimaryQ();
//                System.out.println("Entry ");
//                printEntry();
            } else {
                this.primaryQueue.add(entryReq);
            }
        }
    }

    private void printPrimaryQ () {
        for (int i = 0; i < this.primaryQueue.size(); i++) {
            MethodRequest req = this.primaryQueue.poll();
            System.out.println(i + ". " + req.getTaskType());
            this.primaryQueue.add(req);
        }
    }

    private void printEntry () {
        for (int i = 0; i < this.entryQueue.getSize(); i++) {
            MethodRequest req = this.entryQueue.take();
            System.out.println(i + ". " + req.getTaskType());
            this.entryQueue.put(req);
        }
    }
}
