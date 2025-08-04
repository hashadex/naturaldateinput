package io.github.hashadex.naturaldateinput.parsers.en;

import io.github.hashadex.naturaldateinput.parsers.templates.WeekdayParser;

/**
 * English parser that handles expressions like "on sunday", "on the next friday".
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENWeekdayParser extends WeekdayParser {
    /**
     * Constructs the parser
     * 
     * @since 1.0.0
     */
    public ENWeekdayParser() {
        super(
            """
            (?<=^|\\s)                # Left boundary check
            (?:
                (?:on|the)
                \\s
            ){0,2}                    # Match "on", "the" and "on the"
            (?<nextmodifier>next\\s)? # Next modifier
            (?<weekday>%s)            # Weekday
            (?=$|\\s)                 # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.weekdayMap.keySet())
            ),
            ENConstants.weekdayMap
        );
    }
}
