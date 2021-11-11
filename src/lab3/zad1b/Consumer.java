package lab3.zad1b;

import java.util.Random;

public class Consumer implements Runnable {
   private final Buffer buf;
   private final int id;
    private Random rand = new Random();

    public Consumer(Buffer buf, int id) {
       this.buf = buf;
       this.id = id;
   }

    @Override
    public void run() {
        while(true) {
            try {
                int portion = rand.nextInt(PKProblem.MAX_BUFFER_CAPACITY / 2 - 1) + 1;
                this.buf.take(this.id, portion);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
