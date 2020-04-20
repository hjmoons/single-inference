package dke.executor.experiments.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import dke.executor.experiments.data.cifar10.InputCifar;
import dke.executor.experiments.data.cifar10.InstancesCifar;
import dke.executor.experiments.data.cifar10.OutputCifar;
import dke.executor.experiments.data.cifar10.PredictionsCifar;
import dke.executor.experiments.data.mnist.InputMnist;
import dke.executor.experiments.data.mnist.InstancesMnist;
import dke.executor.experiments.data.mnist.OutputMnist;
import dke.executor.experiments.data.mnist.PredictionsMnist;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class CifarAPIExecutor {
    private String inputTopic;
    private String outputTopic;
    private KafkaConsumer<String, String> kafkaConsumer;
    private KafkaProducer<String, String> kafkaProducer;
    private ModelRequest modelRequest;
    private ObjectMapper objectMapper;

    private InputCifar inputCifar;
    private PredictionsCifar predictionsCifar;

    private OutputCifar outputCifar = new OutputCifar();
    private InstancesCifar instancesCifar = new InstancesCifar();

    public CifarAPIExecutor(String bootstrap, String inputTopic, String outputTopic){
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;

        Properties consumerProp = setConsumerConfig(bootstrap);
        Properties producerProp = setProducerConfig(bootstrap);
        kafkaConsumer = new KafkaConsumer<String, String>(consumerProp);
        kafkaProducer = new KafkaProducer<String, String>(producerProp);
        objectMapper = new ObjectMapper();
    }

    public CifarAPIExecutor load(String servingUrl) {
        this.modelRequest = new ModelRequest(servingUrl);
        return this;
    }

    public void consume(){
        kafkaConsumer.subscribe(Arrays.asList(inputTopic));

        while (true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1000));
            for(ConsumerRecord<String, String> record : records){
                // 스톰 기반 분산 딥러닝 추론 모델 비교 실험 용도
                String inputJson = record.value();
                String outputJson = mnistModel(inputJson);
                kafkaProducer.send(new ProducerRecord<String, String>(outputTopic, outputJson));
            }
        }
    }

    public String mnistModel(String inputJson) {
        String outputJson = null;

        try {
            inputCifar = objectMapper.readValue(inputJson, InputCifar.class);
            instancesCifar.setInstances(inputCifar.getInstances());
            String instancesJson = objectMapper.writeValueAsString(instancesCifar);
            String predictJson  = modelRequest.postData(instancesJson);

            predictionsCifar = objectMapper.readValue(predictJson, PredictionsCifar.class);

            outputCifar.setPredictions(predictionsCifar.getPredictions());
            outputCifar.setInputTime(inputCifar.getInputTime());
            outputCifar.setOutputTime(System.currentTimeMillis());
            outputCifar.setNumber(inputCifar.getNumber());

            outputJson = objectMapper.writeValueAsString(outputCifar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputJson;
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
