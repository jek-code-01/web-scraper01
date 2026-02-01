package org.example;

import org.example.dtos.MarketDto;
import org.example.dtos.MatchDto;
import org.example.dtos.RunnerDto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class MatchFormatter {

    private static final DateTimeFormatter KICKOFF_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 'UTC'").withZone(ZoneOffset.UTC);


    public static String formatMatch(MatchDto match) {
        StringBuilder sb = new StringBuilder();

        sb.append(match.league().sport().name())
                .append(", ")
                .append(match.league().region().name())
                .append(" ")
                .append(match.league().name())
                .append("\n");

        sb.append("  ")
                .append(match.name())
                .append(", ")
                .append(KICKOFF_FORMATTER.format(Instant.ofEpochMilli(match.kickoff())))
                .append(", ")
                .append(match.id())
                .append("\n");

        for (MarketDto market : match.markets()) {
            sb.append("    ").append(market.name()).append("\n");

            for (RunnerDto runner : market.runners()) {
                sb.append("      ")
                        .append(runner.name()).append(", ")
                        .append(runner.price()).append(", ")
                        .append(runner.id())
                        .append("\n");
            }
        }

        return sb.toString();
    }
}
