import com.google.gson.JsonObject;
import net.rithms.riot.api.endpoints.match.dto.Match;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        String gameName = "Ossian";
        String tagLine = "ossi";
        String accountData = RiotAccountApiByGameNameTagLine.getAccountByRiotId(gameName, tagLine);

        System.out.println("Account Data: " + accountData);

        MatchHistoryFetcher fetcherMatchHistory = new MatchHistoryFetcher();

        GetPuuidByGameNameTagLine fetcherPuui = new GetPuuidByGameNameTagLine();

        String puuid = fetcherPuui.fetchPuuid(gameName,tagLine);

        System.out.println(puuid);

        int numberOfMatches = 10;

        List<String> matchIds = fetcherMatchHistory.fetchMatchIds(puuid, numberOfMatches);

        System.out.println("Match IDs: " + matchIds);

        MatchDataFetcher matchFetcher = new MatchDataFetcher();

        JsonObject playerData = matchFetcher.fetchMatchDataForPlayer(matchIds.get(0), puuid);

        if (playerData != null) {
            System.out.println("Player Data: " + playerData.toString());
        } else {
            System.out.println("Failed to retrieve match data for player.");
        }

        matchFetcher.displayPlayerStats(matchIds.get(0),puuid);


    }
}
