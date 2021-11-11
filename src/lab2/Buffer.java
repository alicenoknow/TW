package lab2;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer{
    private int currentBufferSize;
    private int bufferCapacity;
    private final boolean[] waits;

    public Buffer(int threadCount, int bufferCapacity) {

        this.waits = new boolean[threadCount];
        this.bufferCapacity = bufferCapacity;
        this.currentBufferSize = 0;
    }

    public synchronized void put(int id) throws InterruptedException {
        while (this.isEmpty()) {
            waits[id] = true;
            System.out.println("Producer ID: " + id + " Waiting: " + Arrays.toString(waits) + " buffer: " + this.currentBufferSize);
            wait();
        }
        waits[id] = false;
        this.currentBufferSize--;
        notifyAll();
    }

    public synchronized void take(int id) throws InterruptedException {
        while (!this.isEmpty()) {
            waits[id] = true;
            System.out.println("Consumer ID: " + id + " Waiting: " + Arrays.toString(waits) + " buffer: " + this.currentBufferSize);
            wait();
        }
        waits[id] = false;
        this.currentBufferSize++;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return this.currentBufferSize == 0;
    }
}
