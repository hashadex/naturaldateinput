package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.constants.ENConstants;
import me.hashadex.naturaldateinput.parsers.templates.ChronoUnitLaterParser;

public class ENChronoUnitLaterParser extends ChronoUnitLaterParser {
    public ENChronoUnitLaterParser() {
        super(
            """
            (?<=^|\\s)          # Left boundary check
            (?:
                (?:in|after)    # Optionally match "in" or "after" (e.g "in 10 days")
                \\s
            )?
            (?<amount>\\d+)     # Amount of units we need to add to reference date
            \\s
            (?<unit>%s)s?       # Units (e.g. days, weeks, months)
            (?:
                \\s
                (?:later|after) # Optionally match "later" or "after" (e.g. "10 days later")
            )?
            (?=$|\\s)           # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.chronoUnitMap.keySet())
            ),
            ENConstants.chronoUnitMap
        );
    }
}
