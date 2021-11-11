package lab2;

public class Consumer implements Runnable {
   private final Buffer buf;
   private final int id;

   public Consumer(Buffer buf, int id) {
       this.buf = buf;
       this.id = id;
   }

    @Override
    public void run() {
        while(true) {
            try {
                this.buf.take(this.id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
