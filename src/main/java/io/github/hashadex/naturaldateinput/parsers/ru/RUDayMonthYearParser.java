package io.github.hashadex.naturaldateinput.parsers.ru;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

/**
 * Russian parser that handles day-month-year dates like "3-е августа 2025 года".
 * See docs for
 * {@link io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser MonthNameParser}
 * for details about how this parser handles unknown or invalid data.
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RUDayMonthYearParser extends MonthNameParser {
    /**
     * Constructs the parser.
     * 
     * @since 2.1.0
     */
    public RUDayMonthYearParser() {
        super(
            """
            (?<=^|\\s)           # Left boundary check
            (?:
                (?<day>\\d{1,2}) # Day
                (?:-?е)?         # Optionally match ordinal indicator
                \\s
            )?
            (?<month>%s)         # Month
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
            Map.of("day", 1, "month", 2, "year", 3),
            RUConstants.monthMap
        );
    }
}

