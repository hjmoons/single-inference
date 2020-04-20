package dke.executor.experiments;

import dke.executor.experiments.model.CifarAPIExecutor;
import dke.executor.experiments.model.MnistAPIExecutor;

public class ModelExecutorTest {
    public static void main(String[] args) {
        String bootstrap = "MN:49092,SN01:49092,SN02:49092,SN03:49092,SN04:49092,SN05:49092,SN06:49092,SN07:49092,SN08:49092";
        String inputTopic = args[0];
        String outputTopic = args[1];
        String servingAPI = args[2];
        String modelName = args[3];

        if(modelName.equals("mnist")) {
            System.out.println("MNIST model start!!");
            MnistAPIExecutor mnistAPIExecutor = new MnistAPIExecutor(bootstrap, inputTopic, outputTopic).load(servingAPI);
            mnistAPIExecutor.consume();
        } else if(modelName.equals("cifar10")) {
            System.out.println("CIFAR-10 model start!!");
            CifarAPIExecutor cifarAPIExecutor = new CifarAPIExecutor(bootstrap, inputTopic, outputTopic).load(servingAPI);
            cifarAPIExecutor.consume();
        } else {
            System.out.println("Wrong data name is inserted!!");
        }
    }
}
