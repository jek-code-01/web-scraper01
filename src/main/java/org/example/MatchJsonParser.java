package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.MatchDto;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchJsonParser {

    private static final List<String> SPORTS = List.of("Football", "Tennis", "Ice Hockey", "Basketball");
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int MATCHES_PER_LEAGUE = 2;
    //private static final Logger log = Logger.getLogger(MatchJsonParser.class.getName());


    private JsonNode mapStringtoJsonNode(String json) {
        JsonNode root;
        try {
            root = MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON input is incorrect", e);
        }
        return root;
    }


    public List<String> fetchTopLeagues(String leagues) {

        List<String> topLeagueIds = new ArrayList<>();
        JsonNode root = mapStringtoJsonNode(leagues);


        for (String sportName : SPORTS) {
            for (JsonNode sport : root) {
                if (!sportName.equals(sport.path("name").asText()))
                    continue;


                for (JsonNode region : sport.path("regions")) {
                    for (JsonNode league : region.path("leagues")) {
                        if (league.path("top").asBoolean()) {
                            topLeagueIds.add(league.path("id").asText());
                        }
                    }
                }
                break;
            }
        }
        return topLeagueIds;
    }

    public List<String> fetchMatchIds(String matches) {

        JsonNode data = mapStringtoJsonNode(matches).path("data");
        List<String> ids = new ArrayList<>(MATCHES_PER_LEAGUE);

        for (int i = 0; i < Math.min(MATCHES_PER_LEAGUE, data.size()); i++)
            ids.add(data.path(i).path("id").asText());

        return ids;
    }



    public MatchDto parseMatch(String matchJson) {
        try {
            return MAPPER.readValue(matchJson, MatchDto.class);
        } catch (JsonProcessingException e) {
            throw  new IllegalArgumentException("JSON input is incorrect", e);
        }

    }
}
