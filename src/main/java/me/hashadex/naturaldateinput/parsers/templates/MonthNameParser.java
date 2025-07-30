package me.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public abstract class MonthNameParser extends Parser {
    private final Map<String, Month> monthMap;

    protected MonthNameParser(String regex, Map<String, Month> monthMap, int flags) {
        super(regex, flags);

        this.monthMap = monthMap;
    }

    protected MonthNameParser(String regex, Map<String, Month> monthMap) {
        super(regex);

        this.monthMap = monthMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        int startIndex = match.start();
        int endIndex = match.end();

        Month month = monthMap.get(match.group("month").toLowerCase());

        int year = reference.getYear();
        boolean yearSetExplicitly = false;
        if (match.group("year") != null) {
            year = Integer.parseInt(match.group("year"));
            yearSetExplicitly = true;
        }

        int day = 1;
        if (match.group("day") != null) {
            day = Integer.parseInt(match.group("day"));

            if (!YearMonth.of(year, month).isValidDay(day)) {
                // If the day is invalid, (e.g. April 32nd) ignore the day
                // by shifting indexes so the invalid day would not get included
                // in the ParsedComponent
                // e.g. [32 Apr 2025] => 32 [Apr 2025]
                day = 1;

                Map<String, Integer> namedGroups = pattern.namedGroups();

                // Figure out the layout of capturing groups in the regex by comparing
                // the capturing groups' group numbers
                if (namedGroups.get("day") < namedGroups.get("month")) {
                    // Day capturing group is before the month
                    // Shift start index to start index of the capturing group that is after the day group
                    startIndex = match.start(namedGroups.get("day") + 1);
                } else {
                    // Day capturing group is after the month
                    // Shift end index to end index of the capturing group that is before the day group
                    endIndex = match.end(namedGroups.get("day") - 1);
                }

                // If the year is not adjacent to month, (e.g. April 8, 2025)
                // then also ignore the year
                // [April 32, 2025] => [April] 32, 2025
                if (Math.abs(namedGroups.get("month") - namedGroups.get("year")) > 1) {
                    year = reference.getYear();
                    yearSetExplicitly = false;
                }
            }
        }

        // Assemble the date
        LocalDate result = MonthDay.of(month, day).atYear(year);

        if (result.isBefore(reference.toLocalDate()) && !yearSetExplicitly) {
            result = result.plusYears(1);
        }

        // Return the date
        return Optional.of(
            new ParsedComponentBuilder(reference, source, startIndex, endIndex).date(result).build()
        );
    }
}
