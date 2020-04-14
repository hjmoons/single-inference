package dke.executor;

import dke.executor.model.DataPipeline;

public class ModelExecutorMain {
    public static void main(String[] args) {
        String bootstrap = "MN:49092,SN01:49092,SN02:49092,SN03:49092,SN04:49092,SN05:49092,SN06:49092,SN07:49092,SN08:49092";
        String inputTopic = args[0];
        String outputTopic = args[1];
        String servingAPI = args[2];

        DataPipeline inputConsumer = new DataPipeline(bootstrap, inputTopic, outputTopic).InputConsumer(servingAPI);
        inputConsumer.consume();
    }
}
