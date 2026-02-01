package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataScraperControllerTest {

    private final DataRequester requester = mock(DataRequester.class);
    private final MatchJsonParser parser = mock(MatchJsonParser.class);

    private final DataScraperController controller =
            new DataScraperController(requester, parser);

    @Test
    void shouldCollectMatchIdsFromAllLeagues() {

        when(requester.getLeaguesJsonString()).thenReturn("leaguesJson");
        when(parser.fetchTopLeagues("leaguesJson"))
                .thenReturn(List.of("L1", "L2"));

        when(requester.getMatchesFromLeagueJsonString("L1")).thenReturn("m1");
        when(requester.getMatchesFromLeagueJsonString("L2")).thenReturn("m2");

        when(parser.fetchMatchIds("m1")).thenReturn(List.of("A", "B"));
        when(parser.fetchMatchIds("m2")).thenReturn(List.of("C", "D"));

        List<String> result = controller.getIdsOfMatchesParallel();

        assertEquals(List.of("A", "B", "C", "D"), result);
    }

}