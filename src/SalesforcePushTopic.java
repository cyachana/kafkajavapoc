
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectorConfig;

public class SalesforcePushTopic {

    public static void main(String[] args) throws Exception {
        // Set up the Salesforce connection parameters
        String username = "YOUR_SALESFORCE_USERNAME";
        String password = "YOUR_SALESFORCE_PASSWORD";
        String securityToken = "YOUR_SALESFORCE_SECURITY_TOKEN";
        String authEndpoint = "https://login.salesforce.com/services/Soap/u/50.0";

        // Set up the PushTopic object with the necessary fields
        SObject pushTopic = new SObject();
        pushTopic.setType("PushTopic");
        pushTopic.setField("Name", "YOUR_PUSH_TOPIC_NAME");
        pushTopic.setField("Query", "SELECT Id, Name, Field1, Field2 FROM YOUR_OBJECT__c WHERE Field1 = 'Value1'");
        pushTopic.setField("ApiVersion", "50.0");

        // Connect to Salesforce and create the PushTopic
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(username);
        config.setPassword(password + securityToken);
        config.setAuthEndpoint(authEndpoint);
        PartnerConnection connection = Connector.newConnection(config);
        connection.create(new SObject[] {pushTopic});
    }
}
