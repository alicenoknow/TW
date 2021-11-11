package lab3.zad1b;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer{
    private int currentBufferSize;
    private int bufferCapacity;
    private int[] waitingCounter;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition producersCond = lock.newCondition();
    private final Condition consumersCond = lock.newCondition();

    public Buffer(int threadCount, int bufferCapacity) {
        this.bufferCapacity = bufferCapacity;
        this.currentBufferSize = 0;
        this.waitingCounter = new int[threadCount];
        Arrays.fill(this.waitingCounter, 0);
    }

    public void put(int id, int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while (this.currentBufferSize + portion > this.bufferCapacity) {
                this.producersCond.await();
                this.waitingCounter[id]++;
                System.out.println("Producer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
                System.out.println(Arrays.toString(this.waitingCounter));
            }
            this.currentBufferSize += portion;
            this.consumersCond.signal();
        } finally {
            this.waitingCounter[id] = 0;
            this.lock.unlock();
        }
    }

    public void take(int id, int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while (this.currentBufferSize - portion < 0) {
                this.consumersCond.await();
                this.waitingCounter[id]++;
                System.out.println("Consumer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
                System.out.println(Arrays.toString(this.waitingCounter));
            }
            this.currentBufferSize -= portion;
            this.producersCond.signal();
        } finally {
            this.waitingCounter[id] = 0;
            this.lock.unlock();
        }
    }
}
