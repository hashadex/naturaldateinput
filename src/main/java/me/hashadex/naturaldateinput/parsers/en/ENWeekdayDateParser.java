package me.hashadex.naturaldateinput.parsers.en;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.ParseResult;
import me.hashadex.naturaldateinput.parsers.DateParser;

public class ENWeekdayDateParser extends DateParser {
    private static HashMap<String, DayOfWeek> weekdayNames = new HashMap<>();

    static {
        // Monday
        weekdayNames.put("monday", DayOfWeek.MONDAY);
        weekdayNames.put("mon", DayOfWeek.MONDAY);
        // Tuesday
        weekdayNames.put("tuesday", DayOfWeek.TUESDAY);
        weekdayNames.put("tue", DayOfWeek.TUESDAY);
        // Wednesday
        weekdayNames.put("wednesday", DayOfWeek.WEDNESDAY);
        weekdayNames.put("wed", DayOfWeek.WEDNESDAY);
        // Thursday
        weekdayNames.put("thursday", DayOfWeek.THURSDAY);
        weekdayNames.put("thu", DayOfWeek.THURSDAY);
        // Friday
        weekdayNames.put("friday", DayOfWeek.FRIDAY);
        weekdayNames.put("fri", DayOfWeek.FRIDAY);
        // Saturday
        weekdayNames.put("saturday", DayOfWeek.SATURDAY);
        weekdayNames.put("sat", DayOfWeek.SATURDAY);
        // Sunday
        weekdayNames.put("sunday", DayOfWeek.SUNDAY);
        // "sun" is not in the map to avoid confusion with the Sun (the star)
    }

    public ENWeekdayDateParser() {
        super(
            "\\b" +                                                               // word boundary
            "(?:on |the ){0,2}" +                                                 // optionally match "on the"
            "(?:(?<modifier>next) )?" +                                           // next modifier
            "(?<weekday>" + keySetToRegexAlternate(weekdayNames.keySet()) + ")" + // weekday
            "\\b"                                                                 // word boundary
        );
    }

    @Override
    public ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference) {
        ArrayList<ParseResult<LocalDate>> results = new ArrayList<>();
        ArrayList<MatchResult> matches = findAllMatches(input);

        for (MatchResult match : matches) {
            boolean nextModifier = false;

            if (match.group("modifier") != null) {
                nextModifier = true;
            }

            DayOfWeek selected = weekdayNames.get(match.group("weekday").toLowerCase());

            LocalDate result = reference.toLocalDate().with(
                TemporalAdjusters.nextOrSame(selected)
            );

            if (nextModifier) {
                result = result.with(
                    TemporalAdjusters.next(selected)
                );
            }

            results.add(
                new ParseResult<LocalDate>(reference, input, match, result)
            );
        }

        return results;
    }
}
