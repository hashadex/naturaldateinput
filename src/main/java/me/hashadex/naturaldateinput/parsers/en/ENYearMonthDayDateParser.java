package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.constants.ENConstants;
import me.hashadex.naturaldateinput.parsers.templates.MonthNameDateParser;

public class ENYearMonthDayDateParser extends MonthNameDateParser {
    public ENYearMonthDayDateParser() {
        super(
            """
            (?<=^|\\s) # Left boundary check
            (?:
                (?<year>\\d{4})
                \\s
            )?
            (?<month>%s)
            (?:
                \\s
                (?<day>\\d{1,2})
                (?:%s)? # Optionally match ordinal indicator
            )?
            (?=$|\\s) # Right boundary check
            """.formatted(
              setToRegexAlternate(ENConstants.monthMap.keySet()),
              setToRegexAlternate(ENConstants.ordinalIndicators)
            ),
            ENConstants.monthMap
        );
    }
}
