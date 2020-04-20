package dke.executor.experiments.data.cifar10;

public class InputCifar {
    private int[][][][] instances;
    private Long inputTime;
    private int number;

    public int[][][][] getInstances() {
        return instances;
    }

    public void setInstances(int[][][][] instances) {
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
