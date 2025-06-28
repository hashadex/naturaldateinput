package me.hashadex.naturaldateinput.parsers.common;

import me.hashadex.naturaldateinput.parsers.templates.HourMinuteSecondParser;

public final class TwentyFourHourTimeParser extends HourMinuteSecondParser {
    public TwentyFourHourTimeParser() {
        super(
            """
            (?<=^|\\s)
            (?<hour>\\d{1,2})
            :
            (?<minute>\\d{2})
            (?:
                :(?<second>\\d{2})
            )?
            (?=$|\\s)
            """
        );
    }   
}
