package io.github.hashadex.naturaldateinput.parsers.ru;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.ChronoUnitLaterParser;

/**
 * Russian parser that handles expressions like "через 10 дней" ("in 10 days").
 * The amount can be written as an integer or as a cardinal number, such as
 * "десять" ("ten"). Cardinal numbers are supported from 0 through 20.
 * <p>
 * The supported timeunits are:
 * <ul>
 * <li>decades
 * <li>years
 * <li>months
 * <li>weeks
 * <li>days
 * <li>hours
 * <li>minutes
 * <li>seconds
 * </ul>
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RUChronoUnitLaterParser extends ChronoUnitLaterParser {
    /**
     * Constructs the parser.
     * 
     * @since 2.1.0
     */
    public RUChronoUnitLaterParser() {
        super(
            """
            (?<=^|\\s)         # Left boundary check
            (?:через\\s)?      # Optionally match "через" ("in"/"after") (e.g. "через 10 дней")
            (?<amount>%s|\\d+) # Amount of units we need to add to reference date, can be a digit or cardinal number
            \\s
            (?<unit>%s)        # Units (e.g. days, weeks, months)
            (?=$|\\s)          # Right boundary check
            """.formatted(
                toRegexAlternate(RUConstants.cardinalNumberMap.keySet()),
                toRegexAlternate(RUConstants.chronoUnitMap.keySet())
            ),
            Map.of("amount", 1, "unit", 2),
            RUConstants.chronoUnitMap,
            RUConstants.cardinalNumberMap
        );
    }
}
