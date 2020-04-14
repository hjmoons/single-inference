package dke.executor.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dke.executor.data.mnist.InputMnist;
import dke.executor.data.mnist.OutputMnist;
import dke.executor.data.mnist.ResultMnist;

import java.io.IOException;

public class MnistConvertor {
    public String getOutputData(float[][] resultValue, int number, Long inputTime, Long outputTime) {
        String outputData = null;

        OutputMnist outputMnist = new OutputMnist();
        outputMnist.setResult(resultValue);
        outputMnist.setNumber(number);
        outputMnist.setInputTime(inputTime);
        outputMnist.setOutputTime(outputTime);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            outputData = objectMapper.writeValueAsString(outputMnist);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return outputData;
    }

    public float[][] getResultValue(String result) {
        float[][] resultValue = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ResultMnist outputMnist = objectMapper.readValue(result, ResultMnist.class);
            resultValue = outputMnist.getPredictions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultValue;
    }

    public String getPostData(float[][][][] data) {
        String postData = null;

        InputMnist inputMnist = new InputMnist();
        inputMnist.setInstances(data);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            postData = objectMapper.writeValueAsString(inputMnist);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return postData;
    }

    public float[][][][] stringToArray(String stringData) {
        float[][][][] dataList = new float[1][28][28][1];
        String str = stringData.replaceAll("[\\[\\]]", "");
        String[] str_split = str.split(",");

        int count = 0;
        for(int i = 0; i < 28; i++) {
            for(int j = 0; j < 28; j++) {
                dataList[0][i][j][0] = Float.parseFloat(str_split[count]);
                count++;
            }
        }
        return dataList;
    }
}
