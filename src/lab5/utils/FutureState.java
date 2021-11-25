package lab5.utils;

public enum FutureState {
    PENDING("Pending"),
    COMPLETED("Completed");

    private String toStr;

    FutureState(String toStr) {
        this.toStr = toStr;
    }

    public String toString() {
        return toStr;
    }
}
