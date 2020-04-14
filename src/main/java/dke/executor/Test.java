package dke.executor;

import org.json.JSONObject;

public class Test {
    public static void main(String[] args) {
        String result = "{\"predictions\": [[6.10869666e-09, 3.16376759e-06, 1.34595011e-05, 0.00047392, 2.25873137e-05, 2.67202068e-07, 7.7311163e-10, 0.99942112, 3.79240482e-05, 2.74792037e-05]}";
        JSONObject resultJSON = new JSONObject(result);
        Object predictions = resultJSON.get("predictions");

    }
}
