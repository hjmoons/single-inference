package dke.executor.model;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class APIExecutor {
    private String inputTopic;
    private String outputTopic;
    private KafkaConsumer<String, String> kafkaConsumer;
    private KafkaProducer<String, String> kafkaProducer;
    private ModelRequest modelRequest;

    public APIExecutor(String bootstrap, String inputTopic, String outputTopic){
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;

        Properties consumerProp = setConsumerConfig(bootstrap);
        Properties producerProp = setProducerConfig(bootstrap);
        kafkaConsumer = new KafkaConsumer<String, String>(consumerProp);
        kafkaProducer = new KafkaProducer<String, String>(producerProp);
    }

    public APIExecutor load(String servingUrl) {
        this.modelRequest = new ModelRequest(servingUrl);
        return this;
    }

    /**
     * 카프카 컨슈머를 통해 입력데이터를 받고, 이를 서빙 모델로 전송하기 위해 API를 실행한다.
     * 데이터는 모델의 입력 형태 그대로 카프카에서 가져오기 때문에 별도의 변환을 필요가 없다.
     * 전송에 성공하면 결과값을 카프카로 다시 전송한다.
     */
    public void consume(){
        kafkaConsumer.subscribe(Arrays.asList(inputTopic));

        while (true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1000));
            for(ConsumerRecord<String, String> record : records){
                String instJson = record.value();
                String predJson = modelRequest.postData(instJson);
                kafkaProducer.send(new ProducerRecord<String, String>(outputTopic, predJson));
            }
        }
    }

    /* Kafka Consumer Configure */
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

    /* Kafka Producer Configure*/
    public Properties setProducerConfig(String bootstrap) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrap);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }


}
