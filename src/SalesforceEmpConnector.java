import com.salesforce.emp.connector.BayeuxParameters;
import com.salesforce.emp.connector.EmpConnector;
import com.salesforce.emp.connector.TopicSubscription;
import com.salesforce.emp.connector.example.BearerTokenProvider;

public class SalesforceEmpConnectorExample {

    public static void main(String[] args) throws Exception {
        // Set up a Bearer Token Provider to provide the access token for authentication
        BearerTokenProvider tokenProvider = new BearerTokenProvider("YOUR_ACCESS_TOKEN");

        // Set up the BayeuxParameters object with the Salesforce endpoint URL and access token provider
        BayeuxParameters params = new BayeuxParameters.Builder()
                .url("https://YOUR_SALESFORCE_INSTANCE_URL/cometd/47.0")
                .bearerToken(tokenProvider)
                .build();

        // Create a new EmpConnector object with the BayeuxParameters
        EmpConnector connector = new EmpConnector(params);

        // Subscribe to a Push Topic with the specified name
        TopicSubscription subscription = connector.subscribe("/topic/YOUR_PUSH_TOPIC_NAME", (message) -> {
            System.out.println(message.getData());
        });

        // Wait for messages to be received
        Thread.sleep(60000);

        // Unsubscribe from the Push Topic
        connector.unsubscribe(subscription);
    }
}
