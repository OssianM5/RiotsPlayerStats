import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetPuuidByGameNameTagLine {
    private static final String ACCOUNT_API_REGION = "europe"; // Adjust based on region

    /**
     * Retrieves the PUUID of a player based on their game name and tag line.
     * @param gameName The game name of the player (e.g., "Player123").
     * @param tagLine The tag line of the player (e.g., "NA1").
     * @return The PUUID of the player, or null if an error occurs.
     */
    public String fetchPuuid(String gameName, String tagLine) {
        String endpoint = String.format("https://%s.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s",
                ACCOUNT_API_REGION, gameName, tagLine);

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Riot-Token", RiotApiConfig.getApiKey()); // Use API key from RiotApiConfig

            // Check for successful response
            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response to extract PUUID
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                return jsonResponse.get("puuid").getAsString();
            } else {
                System.err.println("Error: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if an error occurs
    }
}
