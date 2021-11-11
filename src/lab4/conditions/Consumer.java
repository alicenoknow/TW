package lab4.conditions;

import java.util.Random;

public class Consumer implements Runnable {
   private final Buffer buf;
   private final int id;
    private Random rand = new Random(12);

    public Consumer(Buffer buf, int id) {
       this.buf = buf;
       this.id = id;
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
