package io.github.hashadex.naturaldateinput.parsers.en;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

/**
 * English parser that handles year-month-day dates like "2025 August 3rd".
 * The day and the year are optional. If the day is missing, it is assumed to
 * be 1. If the year is missing, it is implied based on the reference date.
 * <p>
 * If this parser finds an invalid day, it will shift the end index so
 * the invalid day would not get included in the resulting
 * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponent}.
 * For example, the date "2025 April 32nd" would be parsed as "2025 April".
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENYearMonthDayParser extends MonthNameParser {
    /**
     * Constructs the parser.
     * 
     * @since 1.0.0
     */
    public ENYearMonthDayParser() {
        super(
            """
            (?<=^|\\s) # Left boundary check
            (?:
                (?<year>\\d{4})
                \\s
            )?
            (?<month>%s)
            (?:
                \\s
                (?<day>\\d{1,2})
                (?:%s)? # Optionally match ordinal indicator
            )?
            (?=$|\\s) # Right boundary check
            """.formatted(
              toRegexAlternate(ENConstants.monthMap.keySet()),
              toRegexAlternate(ENConstants.ordinalIndicators)
            ),
            Map.of("year", 1, "month", 2, "day", 3),
            ENConstants.monthMap
        );
    }
}
