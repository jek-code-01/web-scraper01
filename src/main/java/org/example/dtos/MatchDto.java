package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record MatchDto(String id, String name, long kickoff, LeagueDto league, List<MarketDto> markets) {
}
