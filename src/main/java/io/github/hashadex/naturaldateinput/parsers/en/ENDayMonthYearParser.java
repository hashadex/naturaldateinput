package io.github.hashadex.naturaldateinput.parsers.en;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

/**
 * English parser that handles day-month-year dates like "3rd of August, 2025".
 * The day and the year are optional. If the day is missing, it is assumed to
 * be 1. If the year is missing, it is implied based on the reference date.
 * <p>
 * If this parser finds an invalid day, it will shift the start index so
 * the invalid day would not get included in the resulting
 * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponent}.
 * For example, the date "32nd of April, 2025" would be parsed as "April, 2025".
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENDayMonthYearParser extends MonthNameParser {
    /**
     * Constructs the parser.
     * 
     * @since 1.0.0
     */
    public ENDayMonthYearParser() {
        super(
            """
            (?<=^|\\s)           # Left boundary check
            (?:
                (?<day>\\d{1,2}) # Day
                (?:%s)?          # Optionally match ordinal indicator
                (?:\\sof)?       # Optionally match " of" (e.g. 8th of April)
                \\s
            )?
            (?<month>%s)         # Month
            (?:
                ,?               # Optionally match comma after month
                \\s
                (?:of\\s)?       # Optionally match " of"
                (?<year>\\d{4})  # Year
            )?
            (?=$|\\s)            # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.ordinalIndicators),
                toRegexAlternate(ENConstants.monthMap.keySet())
            ),
            Map.of("day", 1, "month", 2, "year", 3),
            ENConstants.monthMap
        );
    }
}
