package dke.executor.experiments.data.cifar10;

public class OutputCifar {
    private float[][] predictions;
    private Long inputTime;
    private Long outputTime;
    private int number;

    public float[][] getPredictions() {
        return predictions;
    }

    public void setPredictions(float[][] predictions) {
        this.predictions = predictions;
    }

    public Long getInputTime() {
        return inputTime;
    }

    public void setInputTime(Long inputTime) {
        this.inputTime = inputTime;
    }

    public Long getOutputTime() {
        return outputTime;
    }

    public void setOutputTime(Long outputTime) {
        this.outputTime = outputTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
