package dke.executor.data.mnist;

public class InputMnist {
    private float[][][][] instances = new float[1][28][28][1];

    public void setInstances(float[][][][] instances) {
        this.instances = instances;
    }

    public float[][][][] getInstances() {
        return instances;
    }
}
