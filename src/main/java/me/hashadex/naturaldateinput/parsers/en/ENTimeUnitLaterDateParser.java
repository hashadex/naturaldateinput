package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.ParseResult;
import me.hashadex.naturaldateinput.parsers.DateParser;

public class ENTimeUnitLaterDateParser extends DateParser {
    private static HashMap<String, ChronoUnit> chronoUnits = new HashMap<>();
    
    static {
        chronoUnits.put("day", ChronoUnit.DAYS);
        chronoUnits.put("week", ChronoUnit.WEEKS);
        chronoUnits.put("month", ChronoUnit.MONTHS);
        chronoUnits.put("year", ChronoUnit.YEARS);
    }

    public ENTimeUnitLaterDateParser() {
        //\b(?:in |after )?(\d+) (day|week|month|year)s?(?: later| after| from now| henceforth |forward |out)?\b
        super(
            "\\b" +                                                                 // word boundary
            "(?:in |after )?" +                                                     // optionally match "in" or "after"
            "(?<amount>\\d+) " +                                                    // amount + space
            "(?<timeunit>" + keySetToRegexAlternate(chronoUnits.keySet()) + ")s?" + // timeunit
            "(?: later| after| from now)?" +                                        // optionally match later, after, etc.
            "\\b"                                                                   // word boundary
        );
    }

    @Override
    public ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference) {
        ArrayList<ParseResult<LocalDate>> results = new ArrayList<>();
        ArrayList<MatchResult> matches = findAllMatches(input);

        for (MatchResult match : matches) {
            LocalDate result = reference.toLocalDate().plus(
                Integer.parseInt(match.group("amount")),
                chronoUnits.get(match.group("timeunit"))
            );

            results.add(
                new ParseResult<LocalDate>(reference, input, match, result)
            );
        }

        return results;
    }
}
