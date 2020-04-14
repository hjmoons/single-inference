package dke.executor.data.mnist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MnistConvertor {
    public float[][] getResultValue(String result, long inputTime, int number) {
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

    public String getPostData(String data) {
        String postData = null;

        InputMnist instanceMnist = new InputMnist();
        instanceMnist.setInstances(stringToArray(data));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            postData = objectMapper.writeValueAsString(instanceMnist);
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
