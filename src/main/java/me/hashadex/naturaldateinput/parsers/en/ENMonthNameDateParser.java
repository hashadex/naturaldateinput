package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.constants.ENConstants;
import me.hashadex.naturaldateinput.parsers.templates.MonthNameDateParser;

public class ENMonthNameDateParser extends MonthNameDateParser {
    public ENMonthNameDateParser() {
        super(
            """
            (?<=^|\\s)         # Left boundary check
            (?:
              (?<day>\\d{1,2}) # Day
              (?:%s)?          # Optionally match ordinal indicator
              (?:\\sof)?       # Optionally match " of" (e.g. 8th of April)
              \\s
            )?
            (?<month>%s)       # Month
            (?:
              ,?               # Optionally match comma after month
              \\s
              (?<year>\\d{4})  # Year
            )?
            (?=$|\\s)          # Right boundary check
            """.formatted(
                setToRegexAlternate(ENConstants.ordinalIndicators),
                setToRegexAlternate(ENConstants.monthMap.keySet())
            ),
            ENConstants.monthMap
        );
    }
}
