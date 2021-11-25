package lab5;

import java.util.ArrayList;

/**
 * Active Object
 */

public class PKProblem {
    public static int MAX_BUFFER_CAPACITY = 10;

    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<>();
        int REAL_TIME = 300000;
        int producersCount = 5;
        int consumersCount = 5;
        boolean first = true;

        Servant servant = new Servant();
        Scheduler scheduler = new Scheduler();
        Proxy proxy = new Proxy(scheduler, servant);
        Thread t = new Thread(scheduler);
        t.start();

        for(int i = 0; i < consumersCount; i++) {
            t = new Thread(new Consumer(proxy));
            threads.add(t);
            t.start();
        }

        for(int i = 0; i < producersCount; i++) {
            t = new Thread(new Producer(proxy));
            threads.add(t);
            t.start();
        }

        System.out.println("Totally consumed: " + Scheduler.totalConsumed);
        System.out.println("Totally produced: " + Scheduler.totalProduced);
    }
}
