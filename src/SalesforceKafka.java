
import java.util.Properties;
        import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.salesforce.emp.connector.BearerTokenProvider;
import com.salesforce.emp.connector.EventType;
import com.salesforce.emp.connector.TopicSubscription;
import com.salesforce.emp.connector.example.BearerTokenProvider;

public class SalesforceKafka {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Set up the Salesforce EMP Connector to receive PushTopic messages
        BearerTokenProvider tokenProvider = new BearerTokenProvider("YOUR_ACCESS_TOKEN");
        BayeuxParameters params = new BayeuxParameters.Builder()
                .url("https://YOUR_SALESFORCE_INSTANCE_URL/cometd/47.0")
                .bearerToken(tokenProvider)
                .build();
        EmpConnector connector = new EmpConnector(params);

        // Subscribe to the PushTopic with the specified name
        TopicSubscription subscription = connector.subscribe("/topic/YOUR_PUSH_TOPIC_NAME", EventType.RECORD, (message) -> {
            // Parse the JSON message into a JsonObject
            JsonObject json = JsonParser.parseString(message.getData().toString()).getAsJsonObject();

            // Create a Kafka record with the message as the value and the topic name as the key
            ProducerRecord<String, String> record = new ProducerRecord<>("YOUR_KAFKA_TOPIC_NAME", json.toString());

            // Send the Kafka record to the Kafka broker
            try (KafkaProducer<String, String> producer = createKafkaProducer()) {
                producer.send(record).get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error sending message to Kafka: " + e.getMessage());
            }
        });

        // Wait for messages to be received
        Thread.sleep(60000);

        // Unsubscribe from the Push Topic
        connector.unsubscribe(subscription);
    }

    private static KafkaProducer<String, String> createKafkaProducer() {
        // Set up the Kafka producer properties
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "YOUR_KAFKA_BROKER_URL");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

        // Create a new Kafka producer with the specified properties
        return new KafkaProducer<>(properties);
    }
}
