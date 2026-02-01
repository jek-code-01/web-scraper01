package org.example;

import org.example.dtos.MatchDto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataScraperController {

    private final DataRequester requester;
    private final MatchJsonParser parser;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private static final Logger log = Logger.getLogger(DataScraperController.class.getName());



    public DataScraperController(DataRequester requester, MatchJsonParser parser) {
        this.requester = requester;
        this.parser = parser;
    }

    public DataScraperController() {
        this(new DataRequester(), new MatchJsonParser());
    }


    public List<String> getIdsOfMatchesParallel() {

        List<String> topLeaguesIds = parser.fetchTopLeagues(requester.getLeaguesJsonString());


        List<CompletableFuture<List<String>>> futures =
                topLeaguesIds.stream()
                        .map(leagueId ->
                                CompletableFuture
                                        .supplyAsync(() -> parser.fetchMatchIds(requester.getMatchesFromLeagueJsonString(leagueId)), executor)
                                        .exceptionally(ex -> {
                                            log.log(Level.SEVERE, "Failed get matches for league " + leagueId, ex);
                                            return List.of();
                                        }))
                        .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();
    }

    public List<MatchDto> getMatchesParallel(List<String> matchIds) {


        List<CompletableFuture<MatchDto>> futures = matchIds.stream()
                .map(id ->
                        CompletableFuture
                                .supplyAsync(() -> parser.parseMatch(requester.getMatchJsonString(id)), executor)
                                .exceptionally(ex -> {
                                    log.log(Level.SEVERE, "Failed match " + id, ex);
                                    return null;
                                }))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<MatchDto> formListOfMatches() {
        List<MatchDto> matchDtos;
        try {
            matchDtos = getMatchesParallel(getIdsOfMatchesParallel());
        } finally {
            executor.shutdown();
        }
        return matchDtos;
    }
}
