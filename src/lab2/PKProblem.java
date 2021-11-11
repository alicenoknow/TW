package lab2;

import java.util.ArrayList;
import java.util.Arrays;

public class PKProblem {



    public static void main(String[] args) {
        int producersCount = 2;
        int consumersCount = 5;

        Buffer buf = new Buffer(producersCount + consumersCount, 1);

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
