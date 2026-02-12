package io.github.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import io.github.hashadex.naturaldateinput.parsers.Parser;

/**
 * Base for language parsers that handle date expressions containing a month
 * name, like "August 3rd, 2025".
 * <p>
 * The names of months are stored in a map, passed to the constructor as the
 * <code>monthMap</code> parameter.
 * <p>
 * If this parser finds an invalid day, it will shift the start or end index so
 * the invalid day would not get included. For example, the match "32 April 2025"
 * will be parsed as "April 2025", excluding the invalid day from the resulting
 * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponent}.
 * <p>
 * The regex must contain a <code>month</code> named capturing group that must
 * only match month names from the <code>monthMap</code>. The regex may
 * optionally contain <code>day</code> and <code>year</code> capturing groups.
 * These groups must only capture digits. The capturing groups may be arranged
 * in any order. If the <code>year</code> capturing group is not in the regex
 * or fails to capture anything, the year is implied based on the reference date.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public abstract class MonthNameParser extends Parser {
    private final Map<String, Month> monthMap;

    /**
     * Constructs the parser using custom regex flags. See the
     * {@link MonthNameParser class doc comment} for requirements for the
     * regexes and maps.
     * 
     * @param regex         Regex for the parser
     * @param namedGroupMap Map of capturing groups' names to their indexes
     * @param monthMap      Map of names of months in your language to elements
     *                      of the {@link java.time.Month} enum.
     * @param flags         Bit mask of the regex flags that will be passed
     *                      to {@link java.util.regex.Pattern#compile(String, int)}
     * @since 2.0.0
     */
    protected MonthNameParser(
        String regex,
        Map<String, Integer> namedGroupMap,
        Map<String, Month> monthMap,
        int flags
    ) {
        super(regex, namedGroupMap, flags);

        this.monthMap = monthMap;
    }

    /**
     * Constructs the parser using default regex flags. See the
     * {@link MonthNameParser class doc comment} for requirements for the
     * regexes and maps.
     * 
     * @param regex         Regex for the parser
     * @param namedGroupMap Map of capturing groups' names to their indexes
     * @param monthMap      Map of names of months in your language to elements
     *                      of the {@link java.time.Month} enum.
     * @since 2.0.0
     */
    protected MonthNameParser(
        String regex,
        Map<String, Integer> namedGroupMap,
        Map<String, Month> monthMap
    ) {
        super(regex, namedGroupMap);

        this.monthMap = monthMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        int startIndex = match.start();
        int endIndex = match.end();

        Month month = monthMap.get(match.group(namedGroupMap.get("month")).toLowerCase());

        int year = reference.getYear();
        boolean yearSetExplicitly = false;
        if (namedGroupMap.containsKey("year") && match.group(namedGroupMap.get("year")) != null) {
            year = Integer.parseInt(match.group(namedGroupMap.get("year")));
            yearSetExplicitly = true;
        }

        int day = 1;
        if (namedGroupMap.containsKey("day") && match.group(namedGroupMap.get("day")) != null) {
            day = Integer.parseInt(match.group(namedGroupMap.get("day")));

            if (!YearMonth.of(year, month).isValidDay(day)) {
                // If the day is invalid, (e.g. April 32nd) ignore the day
                // by shifting indexes so the invalid day would not get included
                // in the ParsedComponent
                // e.g. [32 Apr 2025] => 32 [Apr 2025]
                day = 1;

                // Figure out the layout of capturing groups in the regex by comparing
                // the capturing groups' group numbers
                if (namedGroupMap.get("day") < namedGroupMap.get("month")) {
                    // Day capturing group is before the month
                    // Shift start index to start index of the capturing group that is after the day group
                    startIndex = match.start(namedGroupMap.get("day") + 1);
                } else {
                    // Day capturing group is after the month
                    // Shift end index to end index of the capturing group that is before the day group
                    endIndex = match.end(namedGroupMap.get("day") - 1);
                }

                // If the year is not adjacent to month, (e.g. April 8, 2025)
                // then also ignore the year
                // [April 32, 2025] => [April] 32, 2025
                if (Math.abs(namedGroupMap.get("month") - namedGroupMap.get("year")) > 1) {
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
