Data Parser Java

This is a Java application that retrieves prematch sports data using backend JSON endpoints only (no browser, no Selenium, no HTML scraping).
The program runs asynchronous requests (max 3 threads) and prints structured match, market, and odds data to the console.


WHAT THIS PROGRAM DOES

The application:

1. Processes sports in strict order:
   - Football
   - Tennis
   - Hockey
   - Basketball

2. For each sport:
   - Finds leagues marked as Top Leagues

3. For each top league:
   - Takes the first 2 matches

4. For each match:
   - Loads all available markets
   - Extracts:
     - Match name
     - Start date & time (UTC)
     - Match ID
     - Market names
     - Outcome names, odds, and IDs

5. Prints everything to the console in the required format.


HOW TO RUN (STEP-BY-STEP)

STEP 1 — Install Java (REQUIRED)

Check if Java exists:
    java -version

If you see version 17 or higher, continue.

If not installed:
1. Go to https://adoptium.net/
2. Download Java 17 or newer
3. Install it
4. Restart your terminal


STEP 2 — Install Maven (REQUIRED)

Check:
    mvn -version

If not installed:
1. Download Maven from https://maven.apache.org/download.cgi
2. Extract it
3. Add the 'bin' folder to your system PATH
4. Restart terminal


STEP 3 — Download This Project

Option A (ZIP):
- Click green Code button on GitHub
- Click Download ZIP
- Extract it

Option B (Git):
    git clone https://github.com/jek-code-01/web-scraper01.git
    cd web-scraper01

You should see:
    pom.xml
    src/
    README.txt


STEP 4 — Build the Project

Inside the project folder:
    mvn clean package

Wait for:
    BUILD SUCCESS


STEP 5 — Run the Program

Option A (recommended):
    mvn exec:java -Dexec.mainClass="org.example.Main"


Option B (run compiled JAR):
    java -jar target/scrapper01-1.0-SNAPSHOT.jar



TECH DETAILS

- Java 21
- Maven
- Jackson (JSON parsing)
- CompletableFuture
- ExecutorService (max 3 threads)



Educational / test task project.
