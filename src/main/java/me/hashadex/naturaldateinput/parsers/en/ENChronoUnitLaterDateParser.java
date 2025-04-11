package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.constants.ENConstants;
import me.hashadex.naturaldateinput.parsers.templates.ChronoUnitLaterDateParser;

public class ENChronoUnitLaterDateParser extends ChronoUnitLaterDateParser {
    public ENChronoUnitLaterDateParser() {
        super(
            """
            (?<=^|\\s)
            (?:
                (?:in|after)
                \\s
            )?
            (?<amount>\\d+)
            \\s
            (?<unit>%s)s?
            (?:
                \\s
                (?:later|after)
            )?
            (?=$|\\s)
            """.formatted(
                setToRegexAlternate(ENConstants.chronoUnitMap.keySet())
            ),
            ENConstants.chronoUnitMap
        );
    }
}
