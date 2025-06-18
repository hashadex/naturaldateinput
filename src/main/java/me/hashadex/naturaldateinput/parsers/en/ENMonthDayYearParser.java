package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.MonthNameParser;

public class ENMonthDayYearParser extends MonthNameParser{
    public ENMonthDayYearParser() {
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
                toRegexAlternate(ENConstants.monthMap.keySet()),
                toRegexAlternate(ENConstants.ordinalIndicators)
            ),
            ENConstants.monthMap
        );
    }
}
