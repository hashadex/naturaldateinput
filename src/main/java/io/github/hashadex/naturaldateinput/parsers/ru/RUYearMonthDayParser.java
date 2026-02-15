package io.github.hashadex.naturaldateinput.parsers.ru;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

/**
 * Russian parser that handles year-month-day dates like "2025 август 3".
 * See docs for
 * {@link io.github.hashadex.naturaldateinput.parsers.templates.MonthNameParser MonthNameParser}
 * for details about how this parser handles unknown or invalid data.
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RUYearMonthDayParser extends MonthNameParser {
    /**
     * Constructs the parser.
     * 
     * @since 2.1.0
     */
    public RUYearMonthDayParser() {
        super(
            """
            (?<=^|\\s)           # Left boundary check
            (?:
                (?<year>\\d{4})
                (?:-?го)?        # Optionally match ordinal indicator
                (?:\\sгода)?     # Optionally match "года" ("year")
                \\s
            )?
            (?<month>%s)
            (?:
                \\s
                (?<day>\\d{1,2})
                (?:-?е)?         # Optionally match ordinal indicator
            )?
            (?=$|\\s) # Right boundary check
            """.formatted(
              toRegexAlternate(RUConstants.monthMap.keySet())
            ),
            Map.of("year", 1, "month", 2, "day", 3),
            RUConstants.monthMap
        );
    }
}
