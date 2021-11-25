package lab5;

import lab5.utils.FutureState;

public class Future {
    private FutureState state;

    public Future(){
        this.state = FutureState.PENDING;
    }

    public boolean isReady() {
        return this.state == FutureState.COMPLETED;
    }

    public void complete() {
        this.state = FutureState.COMPLETED;
    }
}