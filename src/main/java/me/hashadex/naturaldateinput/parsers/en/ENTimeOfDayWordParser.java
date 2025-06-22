package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.TimeOfDayWordParser;

public class ENTimeOfDayWordParser extends TimeOfDayWordParser {
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
