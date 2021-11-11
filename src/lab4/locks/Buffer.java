package lab4.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer{
    public static int totalProduced = 0;
    public static int totalConsumed = 0;
    private int currentBufferSize;
    private int bufferCapacity;
    private final ReentrantLock innerLock = new ReentrantLock(true);
    private final ReentrantLock consumersLock = new ReentrantLock(true);
    private final ReentrantLock producersLock = new ReentrantLock(true);
    private final Condition waiterCond = innerLock.newCondition();

    public Buffer() {
        this.bufferCapacity = PKProblem.MAX_BUFFER_CAPACITY;
        this.currentBufferSize = 0;
    }

    public void put(int portion) throws InterruptedException {
        try {
            this.producersLock.lock();
            try {
                this.innerLock.lock();
                while (this.currentBufferSize + portion > this.bufferCapacity) {
                    this.waiterCond.await();
                }
                this.currentBufferSize += portion;
                this.totalProduced += portion;
                this.waiterCond.signal();

            }  catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            } finally {
                this.innerLock.unlock();
            }
        } finally {
            this.producersLock.unlock();
        }
    }

    public void take(int portion) throws InterruptedException {
        try {
            this.consumersLock.lock();
            try {
                this.innerLock.lock();
                while (this.currentBufferSize - portion < 0) {
                    this.waiterCond.await();
                }
                this.currentBufferSize -= portion;
                this.totalConsumed += portion;
                this.waiterCond.signal();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            } finally {
                this.innerLock.unlock();
            }
        } finally {
            this.consumersLock.unlock();
        }
    }
}
