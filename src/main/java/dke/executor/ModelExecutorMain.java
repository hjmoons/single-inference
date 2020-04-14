package dke.executor;

import dke.executor.model.DataPipeline;

public class ModelExecutorMain {
    public static void main(String[] args) {
        String bootstrap = "114.70.235.43:19092,114.70.235.43:19093,114.70.235.43:19094,114.70.235.43:19095,114.70.235.43:19096,114.70.235.43:19097,114.70.235.43:19098,114.70.235.43:19099,114.70.235.43:19100";
        String inputTopic = "mnist-input";
        String outputTopic = "mnist-output";
        String servingAPI = "http://114.70.235.43:8501/v1/models/mnist:predict";

        DataPipeline inputConsumer = new DataPipeline(bootstrap, inputTopic, outputTopic).InputConsumer(servingAPI);
        inputConsumer.consume();
    }
}
