package lab1;

public class Value {
    private int value;

    public Value(){
        this.value = 0;
    }

    public void increament() {
        this.value++;
    }

    public void decreament() {
        this.value--;
    }

    public void printValue() {
        System.out.println(this.value);
    }
}
