package io.github.hashadex.naturaldateinput.parsers.ru;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.TimeOfDayWordParser;

/**
 * Russian parser that handles time-of-day words like "утро" ("morning") and
 * "полдень" ("noon"). The supported words are:
 * <ul>
 * <li>"полночь" for 0:00
 * <li>"утро"/"утром" for 6:00
 * <li>"полдень" for 12:00
 * <li>"вечер"/"вечером" for 20:00
 * </ul>
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RUTimeOfDayWordParser extends TimeOfDayWordParser {
    /**
     * Constructs the parser.
     * 
     * @since 2.1.0
     */
    public RUTimeOfDayWordParser() {
        super(
            """
            (?<=^|\\s)  # Left boundary check
            (?<word>%s) # Time of day word
            (?=$|\\s)   # Right boundary check
            """.formatted(
                toRegexAlternate(RUConstants.timeOfDayWordMap.keySet())
            ),
            Map.of("word", 1),
            RUConstants.timeOfDayWordMap
        );
    }
}
