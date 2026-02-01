package org.example;

import org.example.dtos.MatchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchJsonParserTest {

    private MatchJsonParser parser;

    @BeforeEach
    void setup() {
        parser = new MatchJsonParser();
    }

    private String load(String name) throws Exception {
        return Files.readString(Path.of("src/test/resources/" + name));
    }

    @Test
    void shouldFetchTopLeagues() throws Exception {
        String json = load("SportsJson");

        List<String> leagues = parser.fetchTopLeagues(json);

        assertFalse(leagues.isEmpty());
    }

    @Test
    void shouldFetchOnlyTwoMatchIds() throws Exception {
        String json = load("LeagueJson");

        List<String> ids = parser.fetchMatchIds(json);

        assertEquals(2, ids.size());
    }

    @Test
    void shouldParseMatch() throws Exception {
        String json = load("MatchJson");

        MatchDto match = parser.parseMatch(json);

        assertNotNull(match);
        assertNotNull(match.id());
        assertNotNull(match.markets());
        assertFalse(match.markets().isEmpty());
    }

    @Test
    void shouldParseMarketsAndRunners() throws Exception {
        String json = load("MatchJson");

        MatchDto match = parser.parseMatch(json);

        assertFalse(match.markets().get(0).runners().isEmpty());
    }
}