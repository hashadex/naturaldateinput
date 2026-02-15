package io.github.hashadex.naturaldateinput.parsers.ru;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

/**
 * Russian parser that handles month-day-year dates like "Августа 3, 2025".
 * See docs for
 * {@link io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser MonthNameParser}
 * for details about how this parser handles unknown or invalid data.
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RUMonthDayYearParser extends MonthNameParser {
    /**
     * Constructs the parser
     * 
     * @since 2.1.0
     */
    public RUMonthDayYearParser() {
        super(
            """
            (?<=^|\\s)           # Left boundary check
            (?<month>%s)         # Month
            (?:
                \\s
                (?<day>\\d{1,2}) # Day
                (?:-?е)?         # Optionally match ordinal indicator
            )?
            (?:
                ,?               # Optionally match comma after month
                \\s
                (?<year>\\d{4})  # Year
                (?:-?го)?        # Optionally match ordinal indicator
                (?:\\sгода)?     # Optionally match "года" ("year")
            )?
            (?=$|\\s)            # Right boundary check
            """.formatted(
                toRegexAlternate(RUConstants.monthMap.keySet())
            ),
            Map.of("month", 1, "day", 2, "year", 3),
            RUConstants.monthMap
        );
    }
}

