package dke.executor.main;

import dke.executor.main.model.APIExecutor;

public class ModelExecutorMain {
    public static void main(String[] args) {
        String bootstrap = "MN:49092,SN01:49092,SN02:49092,SN03:49092,SN04:49092,SN05:49092,SN06:49092,SN07:49092,SN08:49092";
        String inputTopic = args[0];
        String outputTopic = args[1];
        String servingAPI = args[2];

        APIExecutor inputConsumer = new APIExecutor(bootstrap, inputTopic, outputTopic).load(servingAPI);
        inputConsumer.consume();
    }
}
