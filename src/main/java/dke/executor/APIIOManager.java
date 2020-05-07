package dke.executor;

import dke.executor.model.APIExecutor;

public class APIIOManager {
    public static void main(String[] args) {
        String bootstrap = "";
        String inputTopic = args[0];
        String outputTopic = args[1];
        String servingAPI = args[2];

        APIExecutor inputConsumer = new APIExecutor(bootstrap, inputTopic, outputTopic).load(servingAPI);
        inputConsumer.consume();
    }
}
