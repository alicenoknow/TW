package lab3.zad2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Zakleszczenie związane z długośćią bufora:
 * - długość bufora M
 * 1. Producent 1 produkuje M/2
 * 2. Consumer chce wziąć M+1 -> nie ma tyle zasobów -> czeka
 * 3. Producent chce wyprodukować M+1 -> nie ma tyle miesjca -> czeka
 * 4. Żaden z procesów nie może wykonać swojego zadania -> zakleszczenie
 *
 * Zagłodzenie w przypadku 2 Condition, scenariusz:
 * 1. W buforze są 3 elementy
 * 2. Consumer1 żąda 5 elementów -> await
 * 3. Consumer2 żąda 3 elementów -> dostaje | bufor = 0
 * 4. Producent produkuje 3 elementy
 * 5. powtarza się 2. 3. 4. a Consumer1 nigdy nie wykona zadania -> zagłodzenie
 *
 * Zagłodzenia dla 2 i 4 conditions:
 * - w przypadku 2 conditions zagładzani są ci z większymi porcjami.
 *
 * Dlaczego hasWaiters doprowadza do zakleszczenia?
 * Bufor: 0 | MaxBufor: 10
 * 0. P1, C1, C2 na locku
 * 1. wchodzi C1 chce 5 -> nie ma -> idzie do firstConsumer
 * 2. wchodzi P1 i produkuje 2 -> wzbudza C1
 * 3. C1, C2, P1 na locku, bufor: 2
 * 4. wchodzi C2 chce 3 -> firstConsumer puste -> nie ma 3 w buforze -> idzie do firstConsumer
 * 5. wchodzi C1 chce 5 -> nie ma,  idzie do firstConsumer
 * 6. wchodzi P1 i produkuje 2 -> wzbudza C1 -> wychodzi
 * 7. P1, C1 na locku, bufor: 4
 * 8. wchodzi C1 -> nie ma 5 > 4 -> wraca do firstConsumer
 * 9. wchodzi P1 produkuje 5 -> wzbudza C2 -> wychodzi
 * 10. C2, P1 na locku, bufor: 9
 * 10. wchodzi C2 -> bierze 3 -> wychodzi
 * 11. wchodzi C2 -> idzie do otherConsumers bo firstConsumer zajęty
 * 12. wchodzi P1 i chce wstawić 5 (bufor: 6) -> idzie do firstProducer
 */

public class Buffer{
    private int currentBufferSize;
    private int bufferCapacity;
//    private int[] waitingCounter;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition producersCond = lock.newCondition();
    private final Condition consumersCond = lock.newCondition();
    private final Condition firstProducersCond = lock.newCondition();
    private final Condition firstConsumersCond = lock.newCondition();

    public Buffer(int threadCount) {
        this.bufferCapacity = PKProblem.MAX_BUFFER_CAPACITY;
        this.currentBufferSize = 0;
//        this.waitingCounter = new int[threadCount];
//        Arrays.fill(this.waitingCounter, 0);
    }

    public void put(int id, int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while(this.lock.hasWaiters(this.firstProducersCond)) {
                this.producersCond.await();
//                this.waitingCounter[id]++;
//                System.out.println(Arrays.toString(this.waitingCounter));
//                System.out.println("Producer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            while (this.currentBufferSize + portion > this.bufferCapacity) {
                this.firstProducersCond.await();
//                this.waitingCounter[id]++;
//                System.out.println("First producer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            this.currentBufferSize += portion;
            this.producersCond.signal();
            this.firstConsumersCond.signal();
        } finally {
//            this.waitingCounter[id] = 0;
            this.lock.unlock();
        }
    }

    public void take(int id, int portion) throws InterruptedException {
        try {
            this.lock.lock();
            while (this.lock.hasWaiters(this.firstConsumersCond)) {
                this.consumersCond.await();
//                this.waitingCounter[id]++;
//                System.out.println(Arrays.toString(this.waitingCounter));
//                System.out.println("Consumer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            while (this.currentBufferSize - portion < 0) {
                this.firstConsumersCond.await();
//                this.waitingCounter[id]++;
//                System.out.println("First consumer ID: " + id + " buffer: " + this.currentBufferSize + " wants: " + portion + " waits for: " + this.waitingCounter[id]);
            }
            this.currentBufferSize -= portion;
            this.consumersCond.signal();
            this.firstProducersCond.signal();
        } finally {
//            this.waitingCounter[id] = 0;
            this.lock.unlock();
        }
    }
}
