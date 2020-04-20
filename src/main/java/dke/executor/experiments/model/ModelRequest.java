package dke.executor.experiments.model;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ModelRequest {
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;

    public ModelRequest(String servingUrl) {
        this.httpClient = HttpClients.createDefault();
        this.httpPost = new HttpPost(servingUrl);
        this.httpPost.setHeader("content-type", "application/json");
    }

    public String postData(String data) {
        String response = null;

        try {
            httpPost.setEntity(new StringEntity(data));

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();

            response = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
