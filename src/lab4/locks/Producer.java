package lab4.locks;

import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buf;
    private Random rand = new Random(12);

    public Producer(Buffer buf) {
        this.buf = buf;
    }

    @Override
    public void run() {
        while(true) {
            try {
                int portion = rand.nextInt(PKProblem.MAX_BUFFER_CAPACITY / 2 - 1) + 1;
                this.buf.put(portion);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
