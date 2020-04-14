package dke.executor.data.mnist;

public class ResultMnist {
    private float[][] predictions = new float[1][10];

    public float[][] getPredictions() {
        return predictions;
    }

    public void setPredictions(float[][] predictions) {
        this.predictions = predictions;
    }
}
