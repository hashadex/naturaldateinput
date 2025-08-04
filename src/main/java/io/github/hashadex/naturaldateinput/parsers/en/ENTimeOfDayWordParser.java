package io.github.hashadex.naturaldateinput.parsers.en;

import io.github.hashadex.naturaldateinput.parsers.templates.TimeOfDayWordParser;

/**
 * English parser that handles time-of-day words like "morning" and "noon". The
 * supported words are:
 * <ul>
 * <li>"midnight" for 0:00
 * <li>"morning" for 6:00
 * <li>"noon" for 12:00
 * <li>"midday" for 12:00
 * <li>"afternoon" for 15:00
 * <li>"evening" for 20:00
 * </ul>
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENTimeOfDayWordParser extends TimeOfDayWordParser {
    /**
     * Constructs the parser
     * 
     * @since 1.0.0
     */
    public ENTimeOfDayWordParser() {
        super(
            """
            (?<=^|\\s)  # Left boundary check
            (?<word>%s) # Time of day word
            (?=$|\\s)   # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.timeOfDayWordMap.keySet())
            ),
            ENConstants.timeOfDayWordMap
        );
    }
}
