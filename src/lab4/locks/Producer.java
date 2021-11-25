package lab4.locks;

import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buf;
    private Random rand = new Random(12);
    private double c;

    public Producer(Buffer buf) {
        this.buf = buf;
    }

    @Override
    public void run() {
        while(true) {
            try {
                int portion = rand.nextInt(PKProblem.MAX_BUFFER_CAPACITY / 2 - 1) + 1;
                this.buf.put(portion);

                for (int i = 0; i < portion * 1000; i++) {
                    c = Math.atan(Math.tan(Math.atan(42099769.42099769)));
                }
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }
}
