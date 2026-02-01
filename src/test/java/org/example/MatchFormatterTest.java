package org.example;

import org.example.dtos.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchFormatterTest {

    @Test
    void testFormatMatch() {
        RunnerDto runner = new RunnerDto("Player 1", "1.5", "r1");
        MarketDto market = new MarketDto("Winner", List.of(runner));
        LeagueDto league = new LeagueDto("Premier League", new SportDto("Football"), new RegionDto("England"));
        MatchDto match = new MatchDto("m1",  "Team A vs Team B", Instant.parse("2026-02-01T15:30:00Z").toEpochMilli(), league, List.of(market));

        String formatted = MatchFormatter.formatMatch(match);

        assertTrue(formatted.contains("Football, England Premier League"));
        assertTrue(formatted.contains("Team A vs Team B, 2026-02-01 15:30 UTC, m1"));
        assertTrue(formatted.contains("Winner"));
        assertTrue(formatted.contains("Player 1, 1.5, r1"));
    }

}