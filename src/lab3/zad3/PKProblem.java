package lab3.zad3;

/**
 * Rózna liczba producentów i konsumentów.
 * Bufor wieloelementowy
 * Porcje losowe < M/2
 * Na Lockach i Conditions
 * 4 Conditions i boolean zamiast hasWaiters
 * Wypisuje kto ile czeka
 */

public class PKProblem {
    public static int MAX_BUFFER_CAPACITY = 10;

    public static void main(String[] args) {
        int producersCount = 5;
        int consumersCount = 5;

        Buffer buf = new Buffer(producersCount + consumersCount);
        for(int i = 0; i < consumersCount; i++) {
            Thread t = new Thread(new Consumer(buf, i));
            t.start();
        }

        for(int i = 0; i < producersCount; i++) {
            Thread t = new Thread(new Producer(buf, i + consumersCount));
            t.start();
        }
    }
}
