package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

public final class ENDayMonthYearParser extends MonthNameParser {
    public ENDayMonthYearParser() {
        super(
            """
            (?<=^|\\s)           # Left boundary check
            (?:
                (?<day>\\d{1,2}) # Day
                (?:%s)?          # Optionally match ordinal indicator
                (?:\\sof)?       # Optionally match " of" (e.g. 8th of April)
                \\s
            )?
            (?<month>%s)         # Month
            (?:
                ,?               # Optionally match comma after month
                \\s
                (?:of\\s)?       # Optionally match " of"
                (?<year>\\d{4})  # Year
            )?
            (?=$|\\s)            # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.ordinalIndicators),
                toRegexAlternate(ENConstants.monthMap.keySet())
            ),
            ENConstants.monthMap
        );
    }
}
