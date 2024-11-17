import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RiotAccountApiByGameNameTagLine {

    public static String getAccountByRiotId(String gameName, String tagLine) {
        String endpoint = String.format("https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s", gameName, tagLine);

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Riot-Token", RiotApiConfig.getApiKey()); // Use the static method to get the API key

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();  // JSON response with account details
            } else {
                System.err.println("Error: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}