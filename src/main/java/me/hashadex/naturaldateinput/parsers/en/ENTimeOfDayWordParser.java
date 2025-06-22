package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.TimeOfDayWordParser;

public class ENTimeOfDayWordParser extends TimeOfDayWordParser {
    public ENTimeOfDayWordParser() {
        super(
            """
            (?<=^|\\s)
            (?<word>%s)
            (?=$|\\s)
            """.formatted(
                toRegexAlternate(ENConstants.timeOfDayWordMap.keySet())
            ),
            ENConstants.timeOfDayWordMap
        );
    }
}
