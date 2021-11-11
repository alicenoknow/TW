package lab4.locks;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

/**
 * Rózna liczba producentów i konsumentów.
 * Bufor wieloelementowy
 * Porcje losowe < M/2
 * Na 3 Lockach i 1 Condition
 * Z pomiarem czasu
 * Totally consumed: 767649124
 * Totally produced: 767649814
 */

public class PKProblem {
    public static int MAX_BUFFER_CAPACITY = 10;

    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<>();
        int REAL_TIME = 300000;
        int producersCount = 5;
        int consumersCount = 5;

        Buffer buf = new Buffer();
        for(int i = 0; i < consumersCount; i++) {
            Thread t = new Thread(new Consumer(buf));
            threads.add(t);
            t.start();
        }

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIDs = threadMXBean.getAllThreadIds();

        for(int i = 0; i < producersCount; i++) {
            Thread t = new Thread(new Producer(buf));
            threads.add(t);
            t.start();
        }

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

        System.out.println("Totally consumed: " + Buffer.totalConsumed);
        System.out.println("Totally produced: " + Buffer.totalProduced);
        System.out.printf("Total cpu time: %s ms. Real time: %s ms.\n", nano / 1E6, REAL_TIME);
    }
}
