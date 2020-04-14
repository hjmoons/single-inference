package dke.executor.data.mnist;

public class OutputMnist {
    private float[][] result;
    private Long inputTime;
    private Long outputTime;
    private int number;

    public float[][] getResult() {
        return result;
    }

    public void setResult(float[][] result) {
        this.result = result;
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
