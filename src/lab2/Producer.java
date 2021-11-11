package lab2;

public class Producer implements Runnable {
    private final Buffer buf;
    private final int id;

    public Producer(Buffer buf, int id) {
        this.buf = buf;
        this.id = id;
    }

    @Override
    public void run() {
        while(true) {
            try {
                this.buf.put(this.id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
