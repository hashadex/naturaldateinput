package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.parsers.templates.HourMinuteSecondParser;

public class ENTwelveHourTimeParser extends HourMinuteSecondParser {
    public ENTwelveHourTimeParser() {
        super(
            """
            (?<=^|\\s)                # Left boundary check
            (?<hour>\\d{1,2})         # Hour
            (?:                       # Optional minute component
                :                     # Delimeter
                (?<minute>\\d{2})     # Minute
                (?:                   # Optionally match second if minute is present
                    :                 # Delimeter
                    (?<second>\\d{2}) # Second
                )?
            )?
            (?:                       # AM/PM component
                \\s                   # Whitespace
                (?:                   # Match either AM or PM
                    (?<am>a\\.?m\\.?) # Match AM or A.M.
                    |
                    (?<pm>p\\.?m\\.?) # Match PM or P.M.
                )
            )
            (?=$|\\s)                 # Left boundary check
            """
        );
    }
}
