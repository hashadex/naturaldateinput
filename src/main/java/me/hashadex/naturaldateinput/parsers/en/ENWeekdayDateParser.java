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
    private static HashMap<String, DayOfWeek> weekdayNameMap = new HashMap<>();

    static {
        // Monday
        weekdayNameMap.put("monday", DayOfWeek.MONDAY);
        weekdayNameMap.put("mon", DayOfWeek.MONDAY);
        // Tuesday
        weekdayNameMap.put("tuesday", DayOfWeek.TUESDAY);
        weekdayNameMap.put("tue", DayOfWeek.TUESDAY);
        // Wednesday
        weekdayNameMap.put("wednesday", DayOfWeek.WEDNESDAY);
        weekdayNameMap.put("wed", DayOfWeek.WEDNESDAY);
        // Thursday
        weekdayNameMap.put("thursday", DayOfWeek.THURSDAY);
        weekdayNameMap.put("thu", DayOfWeek.THURSDAY);
        // Friday
        weekdayNameMap.put("friday", DayOfWeek.FRIDAY);
        weekdayNameMap.put("fri", DayOfWeek.FRIDAY);
        // Saturday
        weekdayNameMap.put("saturday", DayOfWeek.SATURDAY);
        weekdayNameMap.put("sat", DayOfWeek.SATURDAY);
        // Sunday
        weekdayNameMap.put("sunday", DayOfWeek.SUNDAY);
        // "sun" is not in the map to avoid confusion with the Sun (the star)
    }

    public ENWeekdayDateParser() {
        super(
            "\\b" +                                                                 // word boundary
            "(?:on |the ){0,2}" +                                                   // optionally match "on the"
            "(?:(?<modifier>next) )?" +                                             // next modifier
            "(?<weekday>" + keySetToRegexAlternate(weekdayNameMap.keySet()) + ")" + // weekday
            "\\b"                                                                   // word boundary
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

            DayOfWeek selected = weekdayNameMap.get(match.group("weekday").toLowerCase());

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
