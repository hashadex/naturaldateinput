package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.constants.ENConstants;
import me.hashadex.naturaldateinput.parsers.templates.MonthNameDateParser;

public class ENMonthDayYearDateParser extends MonthNameDateParser{
    public ENMonthDayYearDateParser() {
        super(
            """
            (?<=^|\\s) # Left boundary check
            (?<month>%s)
            (?:
                \\s
                (?<day>\\d{1,2})
                (?:%s)? # Optionally match ordinal indicator
            )?
            (?:
                ,?
                \\s
                (?:of\\s)? 
                (?<year>\\d{4})
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
