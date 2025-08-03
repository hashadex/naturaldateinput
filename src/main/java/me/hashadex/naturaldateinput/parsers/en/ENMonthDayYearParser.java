package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

/**
 * English parser that handles month-day-year dates like "August 3rd, 2025".
 * The day and the year are optional. If the day is missing, it is assumed to
 * be 1. If the year is missing, it is implied based on the reference date.
 * <p>
 * If this parser finds an invalid day, it will shift the end index so
 * the invalid day would not get included in the resulting
 * {@link me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponent}.
 * For example, the date "April 32nd, 2025" would be parsed as "April".
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENMonthDayYearParser extends MonthNameParser {
    /**
     * Constructs the parser
     * 
     * @since 1.0.0
     */
    public ENMonthDayYearParser() {
        super(
            """
            (?<=^|\\s) # Left boundary check
            (?<month>%s)
            (?:
                \\s
                (?<day>\\d{1,2})
                (?:%s)? # Optionally match ordinal indicator
            )?
            (?:
                ,?
                \\s
                (?:of\\s)? 
                (?<year>\\d{4})
            )?
            (?=$|\\s) # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.monthMap.keySet()),
                toRegexAlternate(ENConstants.ordinalIndicators)
            ),
            ENConstants.monthMap
        );
    }
}
