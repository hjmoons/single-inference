package dke.executor.model;

import dke.executor.data.MnistConvertor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class DataPipeline {
    private String inputTopic;
    private String outputTopic;
    private KafkaConsumer<String, String> kafkaConsumer;
    private KafkaProducer<String, String> kafkaProducer;
    private ModelRequest modelLoad;
    private MnistConvertor mnistConvertor;

    public DataPipeline(String bootstrap, String inputTopic, String outputTopic){
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;

        Properties consumerProp = setConsumerConfig(bootstrap);
        Properties producerProp = setProducerConfig(bootstrap);
        kafkaConsumer = new KafkaConsumer<String, String>(consumerProp);
        kafkaProducer = new KafkaProducer<String, String>(producerProp);
        mnistConvertor = new MnistConvertor();
    }

    public DataPipeline InputConsumer(String servingUrl) {
        this.modelLoad = new ModelRequest(servingUrl);
        return this;
    }

    public void consume(){
        kafkaConsumer.subscribe(Arrays.asList(inputTopic));

        while (true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1000));
            for(ConsumerRecord<String, String> record : records){
                String input = record.value();

                JSONObject inputJSON = new JSONObject(input);
                String stringData = inputJSON.getString("data");
                long inputTime = inputJSON.getLong("time");
                int number = inputJSON.getInt("number");

                float[][][][] data = mnistConvertor.stringToArray(stringData);
                String result = modelLoad.postData(mnistConvertor.getPostData(data));

                float[][] resultValue = mnistConvertor.getResultValue(result);
                long outputTime = System.currentTimeMillis();
                String outputData = mnistConvertor.getOutputData(resultValue, number, inputTime, outputTime);

                kafkaProducer.send(new ProducerRecord<String, String>(outputTopic, outputData));
            }
        }
    }

    public Properties setConsumerConfig(String bootstrap) {
        String groupId = UUID.randomUUID().toString();

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrap);
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }

    public Properties setProducerConfig(String bootstrap) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrap);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }


}
