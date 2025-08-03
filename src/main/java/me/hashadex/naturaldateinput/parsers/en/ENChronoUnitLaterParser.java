package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.ChronoUnitLaterParser;

/**
 * English parser that handles expressions like "in 10 days". The amount can be
 * written as an integer or as a cardinal number, such as "ten". Cardinal
 * numbers are supported from 0 through 20.
 * <p>
 * The supported timeunits are:
 * <ul>
 * <li>decades
 * <li>years
 * <li>months
 * <li>weeks
 * <li>days
 * <li>half-days
 * <li>hours
 * <li>minutes
 * <li>seconds
 * </ul>
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENChronoUnitLaterParser extends ChronoUnitLaterParser {
    /**
     * Constructs the parser.
     * 
     * @since 1.0.0
     */
    public ENChronoUnitLaterParser() {
        super(
            """
            (?<=^|\\s)          # Left boundary check
            (?:
                (?:in|after)    # Optionally match "in" or "after" (e.g "in 10 days")
                \\s
            )?
            (?<amount>%s|\\d+)  # Amount of units we need to add to reference date, can be a digit or cardinal number
            \\s
            (?<unit>%s)s?       # Units (e.g. days, weeks, months)
            (?:
                \\s
                (?:later|after) # Optionally match "later" or "after" (e.g. "10 days later")
            )?
            (?=$|\\s)           # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.cardinalNumberMap.keySet()),
                toRegexAlternate(ENConstants.chronoUnitMap.keySet())
            ),
            ENConstants.chronoUnitMap,
            ENConstants.cardinalNumberMap
        );
    }
}
