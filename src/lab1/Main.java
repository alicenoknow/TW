package lab1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    // Starting threads - 2 ways
/*
    private static class SingleThreadRunnable implements  Runnable {
        private int number;

        public SingleThreadRunnable(int number) {
            this.number = number;
        }
        public void run() {
            System.out.println("Thread started " + this.number);
        }
    }

    private static class SingleThread extends  Thread {
        private int number;

        public SingleThread(int number) {
            this.number = number;
        }
        public void run() {
            System.out.println("Thread started " + this.number);
        }
    }
*/
    private static class ValueChanger implements  Runnable {

        private final Value toChange;
        private final int valueChangeCount;

        public ValueChanger(Value toChange, int valueChangeCount) {
            this.toChange = toChange;
            this.valueChangeCount = valueChangeCount;
        }

        public void run() {
            for (int i = 0; i < this.valueChangeCount; i++){
                this.toChange.increament();
            }

            for (int i = 0; i < this.valueChangeCount; i++){
                this.toChange.decreament();
            }
        }
    }

    public static void main(String[] args) {
        int threadsCount = 60;
        int valueChangeCount = 300;

        Value commonValue = new Value();
        List<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < threadsCount; i++) {
            Thread t = new Thread(new ValueChanger(commonValue, valueChangeCount));
            threads.add(t);
            t.start();

        }

        for(Thread t : threads) {
            try {
                t.join();
            }
            catch (InterruptedException ignored) {}
        }
        commonValue.printValue();
    }

}
