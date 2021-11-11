package lab3.zad1a;

/**
 * Rózna liczba producentów i konsumentów.
 * Bufor i porcje jednoelementowe
 * Na Lockach i Conditions
 * 2 Conditions
 * Wypisuje kto aktualnie jest w monitorze a kto czeka.
 */

public class PKProblem {

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
