package lab5;

public class Servant {
    private final int bufferCapacity;
    public int currentBufferSize;

    public Servant() {
        this.bufferCapacity = PKProblem.MAX_BUFFER_CAPACITY;
        this.currentBufferSize = 0;
    }

    public void take(int portion) {
        this.currentBufferSize -= portion;
//        Scheduler.totalConsumed += portion;
    }

    public void put(int portion) {
        this.currentBufferSize += portion;
//        Scheduler.totalProduced += portion;
    }

    public boolean canTake(int portion) {
        return this.currentBufferSize - portion >= 0;
    }

    public boolean canPut(int portion) {
        return this.currentBufferSize + portion <= this.bufferCapacity;
    }
}
