package org.example;


import org.example.dtos.MatchDto;

public class Main {


    public static void main(String[] args) {

        DataScraperController dsc = new DataScraperController();

        for ( MatchDto match : dsc.formListOfMatches())
            System.out.println(MatchFormatter.formatMatch(match));

    }
}
