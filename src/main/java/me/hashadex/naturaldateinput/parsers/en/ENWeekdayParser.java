package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.WeekdayParser;

public final class ENWeekdayParser extends WeekdayParser {
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
