package org.example;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.logging.Logger;

public class DataRequester {

    private final HttpClient client =  HttpClient.newHttpClient();
    private final String getTopLeaguesURL = "https://leonbets.com/api-2/betline/sports?ctag=en-US&flags=urlv2";
    private final String patternURLLeague = "https://leonbets.com/api-2/betline/changes/all?ctag=en-US&vtag=9c2cd386-31e1-4ce9-a140-28e9b63a9300&league_id={0}&hideClosed=true&flags=reg,urlv2,orn2,mm2,rrc,nodup";
    private final String patternURLMatch = "https://leonbets.com/api-2/betline/event/all?ctag=en-US&eventId={0}&flags=reg,urlv2,orn2,mm2,rrc,nodup,smgv2,outv2,wd3";
    private static final int TIMEOUT_MS = 5000;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    private static final Logger log = Logger.getLogger(DataRequester.class.getName());


    public String getMatchJsonString(String matchId) {
        return requestJson(MessageFormat.format(patternURLMatch, matchId));
    }


    public String getMatchesFromLeagueJsonString(String leagueId) {
        return requestJson(MessageFormat.format(patternURLLeague, leagueId));
    }


    public String getLeaguesJsonString() {
        return requestJson(getTopLeaguesURL);
    }

    private String requestJson(String url) {

        HttpRequest request = buildHttpRequest(url);
        IOException lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int status = response.statusCode();

                if (status >= 200 && status < 300)
                    return response.body();


                log.warning("Non-success status " + status + " for " + url + " (attempt " + attempt + "/" + MAX_RETRIES + ")");

            } catch (IOException e) {
                lastException = e;
                log.warning("I/O error requesting " + url + " (attempt " + attempt + "/" + MAX_RETRIES + "): " + e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Thread interrupted during HTTP request", e);
            }


            if (attempt < MAX_RETRIES)
                retryDelay();

        }
        throw new IllegalStateException("Failed to fetch URL after " + MAX_RETRIES + " attempts: " + url, lastException);
    }

    private HttpRequest buildHttpRequest(String url) {
        return HttpRequest.newBuilder(URI.create(url))
                .GET()
                .timeout(Duration.ofMillis(TIMEOUT_MS))
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "application/json")
                .build();
    }

    private void retryDelay() {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted during retry delay", e);
        }
    }
}
