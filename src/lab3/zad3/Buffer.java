package lab3.zad3;

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
    private final Condition firstProducersCond = lock.newCondition();
    private final Condition firstConsumersCond = lock.newCondition();

    private boolean isFirstProducent = false;
    private boolean isFirstConsumer = false;

    public Buffer(int threadCount) {
        this.bufferCapacity = PKProblem.MAX_BUFFER_CAPACITY;
        this.currentBufferSize = 0;
        this.waitingCounter = new int[threadCount];
        Arrays.fill(this.waitingCounter, 0);
    }

    public void put(int id, int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while(this.isFirstProducent) {
                this.producersCond.await();
                this.waitingCounter[id]++;
                System.out.println(Arrays.toString(this.waitingCounter));
                System.out.println("Producer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            this.isFirstProducent = true;
            while (this.currentBufferSize + portion > this.bufferCapacity) {
                this.firstProducersCond.await();
                System.out.println("First producer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            this.currentBufferSize += portion;
            this.producersCond.signal();
            this.firstConsumersCond.signal();
        } finally {
            this.waitingCounter[id] = 0;
            this.isFirstProducent = false;
            this.lock.unlock();
        }
    }

    public void take(int id, int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while (this.isFirstConsumer) {
                System.out.println(Arrays.toString(this.waitingCounter));
                this.consumersCond.await();
                this.waitingCounter[id]++;
                System.out.println(Arrays.toString(this.waitingCounter));
                System.out.println("Consumer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            while (this.currentBufferSize - portion < 0) {
                this.isFirstConsumer = true;
                this.firstConsumersCond.await();
                System.out.println("First consumer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            this.currentBufferSize -= portion;
            this.consumersCond.signal();
            this.firstProducersCond.signal();
        } finally {
            this.waitingCounter[id] = 0;
            this.isFirstConsumer = false;
            this.lock.unlock();
        }
    }
}
