package lab4.locks;

import java.util.Random;

public class Consumer implements Runnable {
   private final Buffer buf;
    private Random rand = new Random(12);

    public Consumer(Buffer buf) {
       this.buf = buf;
   }

    @Override
    public void run() {
        while(true) {
            try {
                int portion = rand.nextInt(PKProblem.MAX_BUFFER_CAPACITY / 2 - 1) + 1;
                this.buf.take(portion);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
