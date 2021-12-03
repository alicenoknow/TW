package lab5;

import java.util.Random;

public class Consumer implements Runnable {
    private final Proxy proxy;
    private final Random rand = new Random(23);
    private double c;

    public Consumer(Proxy proxy) {
        this.proxy = proxy;
   }

    @Override
    public void run() {
        while(true) {
            int portion = rand.nextInt(PKProblem.MAX_BUFFER_CAPACITY / 2 - 1) + 1;
            Future future;
            try {
                future = this.proxy.take(portion);
                int extraWork = portion * 1000;
                while(!future.isReady() || extraWork > 0) {
                    if (extraWork > 0) {
                        c = Math.atan(Math.tan(Math.atan(42099769.42099769)));
                        extraWork--;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Consumer thread interrupted");
            }
        }
    }
}
