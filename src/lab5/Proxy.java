package lab5;

import lab5.utils.TaskType;

public class Proxy {
    private final Scheduler scheduler;
    private final Servant servant;

    public Proxy(Scheduler scheduler, Servant servant){
        this.scheduler = scheduler;
        this.servant = servant;
    }

    public Future put(int portion) throws InterruptedException {
        Future future = new Future();
        MethodRequest task = new MethodRequest(TaskType.PUT, portion, future, this.servant);
        this.scheduler.entryQueue.put(task);
        System.out.println("Producer task placed in entry");
        return future;
    }

    public Future take(int portion) throws InterruptedException {
        Future future = new Future();
        MethodRequest task = new MethodRequest(TaskType.TAKE, portion, future, this.servant);
        this.scheduler.entryQueue.put(task);
        System.out.println("Consumer task placed in entry");

        return future;
    }

}
