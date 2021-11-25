package lab4.conditions;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

/**
 * Rózna liczba producentów i konsumentów.
 * Bufor wieloelementowy
 * Porcje losowe < M/2
 * Na Lockach i Conditions
 * 4 Conditions i boolean zamiast hasWaiters
 * Totally consumed: 240674227 per hour
 * Totally produced: 240674240 per hour
 */

public class PKProblem {
    public static int MAX_BUFFER_CAPACITY = 10;

    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<>();
        int producersCount = 5;
        int consumersCount = 5;
        int REAL_TIME = 300000;

        Buffer buf = new Buffer();
        for(int i = 0; i < consumersCount; i++) {
            Thread t = new Thread(new Consumer(buf, i));
            threads.add(t);
            t.start();
        }

        for(int i = 0; i < producersCount; i++) {
            Thread t = new Thread(new Producer(buf, i + consumersCount));
            threads.add(t);
            t.start();
        }
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIDs = threadMXBean.getAllThreadIds();

        boolean first = true;
        long nano = 0;

        for(Thread t : threads) {
            if(first) {
                try {
                    t.join(REAL_TIME);
                    first = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t.interrupt();
        }
        for (long id : threadIDs) {
            nano += threadMXBean.getThreadCpuTime(id);
        }

        System.out.println("Totally consumed: " + lab4.conditions.Buffer.totalConsumed);
        System.out.println("Totally produced: " + lab4.conditions.Buffer.totalProduced);
        System.out.printf("Total cpu time: %s ms. Real time: %s ms.\n", nano / 1E6, REAL_TIME);
    }
}
