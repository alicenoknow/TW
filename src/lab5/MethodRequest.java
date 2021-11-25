package lab5;

import lab5.utils.TaskType;

public class MethodRequest {
    private final TaskType taskType;
    private final Future future;
    private final int portion;
    private final Servant servant;

    public MethodRequest(TaskType taskType, int portion, Future future, Servant servant){
        this.taskType = taskType;
        this.portion = portion;
        this.future = future;
        this.servant = servant;
    }

    public boolean guard() {
        switch(this.taskType) {
            case PUT -> {
                return this.servant.canPut(this.portion);
            }
            case TAKE -> {
                return this.servant.canTake(this.portion);
            }
            default -> {
                return false;
            }
        }
    }

    public void call() {
        switch (this.taskType) {
            case PUT -> {
                this.servant.put(this.portion);
                this.future.complete();
            }
            case TAKE -> {
                this.servant.take(this.portion);
                this.future.complete();
            }
        }
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

}


