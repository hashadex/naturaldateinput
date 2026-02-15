package io.github.hashadex.naturaldateinput.parsers.ru;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.WeekdayParser;

/**
 * Russian parser that handles expressions like "в воскресенье" ("on sunday"),
 * "в следующую пятницу" ("on the next friday").
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RUWeekdayParser extends WeekdayParser {
    /**
     * Constructs the parser.
     * 
     * @since 2.1.0
     */
    public RUWeekdayParser() {
        super(
            """
            (?<=^|\\s)     # Left boundary check
            (?:в\\s)?      # Optionally match "в" (e.g. "в четверг")
            (?:
                (?<nextmodifier>следующий|следующую|следующее|след\\.?)
                \\s
            )?
            (?<weekday>%s) # Weekday
            (?=$|\\s)      # Right boundary check
            """.formatted(
                toRegexAlternate(RUConstants.weekdayMap.keySet())
            ),
            Map.of("nextmodifier", 1, "weekday", 2),
            RUConstants.weekdayMap
        );
    }
}
