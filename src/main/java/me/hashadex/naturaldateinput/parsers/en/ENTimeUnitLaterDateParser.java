package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.ParseResult;
import me.hashadex.naturaldateinput.parsers.DateParser;

public class ENTimeUnitLaterDateParser extends DateParser {
    public ENTimeUnitLaterDateParser() {
        //\b(?:in |after )?(\d+) (day|week|month|year)s?(?: later| after| from now| henceforth |forward |out)?\b
        super(
            "\\b" +                                                    // word boundary
            "(?:in |after )?" +                                        // optionally match "in" or "after"
            "(?<amount>\\d+) (?<timeunit>day|week|month|year)s?" +     // amount + time unit
            "(?: later| after| from now)?" +                           // optionally match later, after, etc.
            "\\b"                                                      // word boundary
        );
    }

    @Override
    public ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference) {
        ArrayList<ParseResult<LocalDate>> results = new ArrayList<>();
        ArrayList<MatchResult> matches = findAllMatches(input);

        for (MatchResult match : matches) {
            int amount = Integer.parseInt(match.group("amount"));

            LocalDate referenceDate = reference.toLocalDate();
            LocalDate result;

            switch (match.group("timeunit")) {
                case "day":
                    result = referenceDate.plusDays(amount);
                    break;
                case "week":
                    result = referenceDate.plusWeeks(amount);
                    break;
                case "month":
                    result = referenceDate.plusMonths(amount);
                    break;
                case "year":
                    result = referenceDate.plusYears(amount);
                    break;
            
                default:
                    continue;
            }

            results.add(
                new ParseResult<LocalDate>(reference, input, match, result)
            );
        }

        return results;
    }
}
