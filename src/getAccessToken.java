
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public class getAccessToken {

    public static void main(String[] args) throws Exception {
        // Set up the Salesforce OAuth 2.0 endpoint URL, client ID, client secret, and redirect URI
        String oauthUrl = "https://login.salesforce.com/services/oauth2/token";
        String clientId = "YOUR_CLIENT_ID";
        String clientSecret = "YOUR_CLIENT_SECRET";
        String redirectUri = "YOUR_REDIRECT_URI";

        // Set up the parameters for the OAuth 2.0 request
        String username = "YOUR_SALESFORCE_USERNAME";
        String password = "YOUR_SALESFORCE_PASSWORD";
        String securityToken = "YOUR_SALESFORCE_SECURITY_TOKEN";
        String grantType = "password";
        String encodedAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        String urlParameters = "grant_type=" + URLEncoder.encode(grantType, "UTF-8") +
                "&username=" + URLEncoder.encode(username, "UTF-8") +
                "&password=" + URLEncoder.encode(password + securityToken, "UTF-8");

        // Send a POST request to the Salesforce OAuth 2.0 endpoint to obtain an access token
        URL url = new URL(oauthUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
        connection.getOutputStream().write(urlParameters.getBytes());

        // Read the response from the Salesforce OAuth 2.0 endpoint
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        reader.close();

        // Extract the access token from the response
        String accessToken = response.substring(response.indexOf("\"access_token\":\"") + 16, response.indexOf("\",\""));

        // Print the access token
        System.out.println("Access Token: " + accessToken);
    }
}