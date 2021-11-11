package lab4.conditions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer{
    public static int totalProduced = 0;
    public static int totalConsumed = 0;
    private int currentBufferSize;
    private int bufferCapacity;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition producersCond = lock.newCondition();
    private final Condition consumersCond = lock.newCondition();
    private final Condition firstProducersCond = lock.newCondition();
    private final Condition firstConsumersCond = lock.newCondition();

    private boolean isFirstProducent = false;
    private boolean isFirstConsumer = false;

    public Buffer() {
        this.bufferCapacity = PKProblem.MAX_BUFFER_CAPACITY;
        this.currentBufferSize = 0;
    }

    public void put(int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while(this.isFirstProducent) {
                this.producersCond.await();
           }
            while (this.currentBufferSize + portion > this.bufferCapacity) {
                this.isFirstProducent = true;
                this.firstProducersCond.await();
            }
            this.currentBufferSize += portion;
            this.totalProduced += portion;
            this.producersCond.signal();
            this.firstConsumersCond.signal();
        }   catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        } finally {
            this.isFirstProducent = false;
            this.lock.unlock();
        }
    }

    public void take(int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while (this.isFirstConsumer) {
                this.consumersCond.await();
            }
            while (this.currentBufferSize - portion < 0) {
                this.isFirstConsumer = true;
                this.firstConsumersCond.await();
            }
            this.currentBufferSize -= portion;
            this.totalConsumed += portion;
            this.consumersCond.signal();
            this.firstProducersCond.signal();
        }  catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        } finally {
            this.isFirstConsumer = false;
            this.lock.unlock();
        }
    }
}
