import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MatchDataFetcher {

    private static final String MATCH_API_REGION = "europe"; // Region for Europe Nordic & East

    /**
     * Fetches match details for a given match ID and returns data specific to a player identified by PUUID.
     * @param matchId The unique ID of the match.
     * @param puuid The unique PUUID of the player.
     * @return A JSON object with the match data for the specified player.
     */
    public JsonObject fetchMatchDataForPlayer(String matchId, String puuid) {
        String endpoint = String.format("https://%s.api.riotgames.com/lol/match/v5/matches/%s", MATCH_API_REGION, matchId);

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Riot-Token", RiotApiConfig.getApiKey());

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response using Gson's streaming API
                JsonObject matchData = JsonParser.parseString(response.toString()).getAsJsonObject();

                // Extract participant data
                JsonArray participants = matchData.getAsJsonObject("info").getAsJsonArray("participants");
                for (int i = 0; i < participants.size(); i++) {
                    JsonObject participant = participants.get(i).getAsJsonObject();
                    if (participant.get("puuid").getAsString().equals(puuid)) {
                        return participant; // Return the data specific to the player with matching PUUID
                    }
                }
            } else {
                System.err.println("Error: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if an error occurs
    }

    /**
     * Fetches and displays specific stats for a given player in a match.
     * @param matchId The unique match ID.
     * @param puuid The unique PUUID of the player.
     */
    public void displayPlayerStats(String matchId, String puuid) {
        JsonObject playerData = fetchMatchDataForPlayer(matchId, puuid);

        if (playerData != null) {
            // Extract player stats from the match data
            int goldEarned = playerData.get("goldEarned").getAsInt();
            int damageDealt = playerData.get("totalDamageDealt").getAsInt();
            int damageTaken = playerData.get("totalDamageTaken").getAsInt();
            int kills = playerData.get("kills").getAsInt();
            int deaths = playerData.get("deaths").getAsInt();
            int assists = playerData.get("assists").getAsInt();
            int visionScore = playerData.get("visionScore").getAsInt();

            // Extract items (0-6)
            JsonArray items = new JsonArray();
            for (int i = 0; i <= 6; i++) {
                items.add(playerData.get("item" + i).getAsInt());
            }

            // Print player stats
            System.out.println("Player Stats:");
            System.out.println("Gold Earned: " + goldEarned);
            System.out.println("Damage Dealt: " + damageDealt);
            System.out.println("Damage Taken: " + damageTaken);
            System.out.println("Kills: " + kills);
            System.out.println("Deaths: " + deaths);
            System.out.println("Assists: " + assists);
            System.out.println("Vision Score: " + visionScore);
            System.out.println("Items Owned: " + items.toString());
        } else {
            System.out.println("Player data not found.");
        }
    }
}
