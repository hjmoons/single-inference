package dke.executor.data.mnist;

public class InputMnist {
    private float[][][][] instances;
    private Long inputTime;
    private int number;

    public float[][][][] getInstances() {
        return instances;
    }

    public void setInstances(float[][][][] instances) {
        this.instances = instances;
    }

    public Long getInputTime() {
        return inputTime;
    }

    public void setInputTime(Long inputTime) {
        this.inputTime = inputTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
