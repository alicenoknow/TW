package lab3.zad1a;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer{
    private int currentBufferSize;
    private boolean[] isWaiting;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition producersCond = lock.newCondition();
    private final Condition consumersCond = lock.newCondition();

    public Buffer(int threadCount) {
        this.currentBufferSize = 0;
        this.isWaiting = new boolean[threadCount];
        Arrays.fill(this.isWaiting, false);
    }

    public void put(int id) throws InterruptedException {
        try {
            this.lock.lock();
            while (!this.isEmpty()) {
                this.isWaiting[id] = true;
                System.out.println("Producer ID: " + id + " buffer: " + this.currentBufferSize);
                this.producersCond.await();
            }
            this.isWaiting[id] = false;
            System.out.println(Arrays.toString(this.isWaiting));
            this.currentBufferSize++;
            this.consumersCond.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public void take(int id) throws InterruptedException {
        try {
            this.lock.lock();
            while (this.isEmpty()) {
                this.isWaiting[id] = true;
                System.out.println("Consumer ID: " + id + " buffer: " + this.currentBufferSize);
                this.consumersCond.await();
            }
            this.isWaiting[id] = false;
            System.out.println(Arrays.toString(this.isWaiting));
            this.currentBufferSize--;
            this.producersCond.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isEmpty() {
        return this.currentBufferSize == 0;
    }
}
