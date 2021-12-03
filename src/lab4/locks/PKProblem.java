package lab4.locks;

import java.util.ArrayList;

/**
 * Rózna liczba producentów i konsumentów.
 * Bufor wieloelementowy
 * Porcje losowe < M/2
 * Na 3 Lockach i 1 Condition
 */

public class PKProblem {
    public static int MAX_BUFFER_CAPACITY = 30;

    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<>();
        int REAL_TIME = 300000;
        int producersCount = 20;
        int consumersCount = 20;
        boolean first = true;

        Buffer buf = new Buffer();
        for(int i = 0; i < consumersCount; i++) {
            Thread t = new Thread(new Consumer(buf));
            threads.add(t);
            t.start();
        }

        for(int i = 0; i < producersCount; i++) {
            Thread t = new Thread(new Producer(buf));
            threads.add(t);
            t.start();
        }

        for(Thread t : threads) {
            if(first) {
                try {
                    t.join(REAL_TIME);
                    first = false;

                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                }
            }
            t.interrupt();
        }
        System.out.println("Totally consumed: " + Buffer.totalConsumed);
        System.out.println("Totally produced: " + Buffer.totalProduced);
    }
}
