package io.github.hashadex.naturaldateinput.parsers.en;

import io.github.hashadex.naturaldateinput.parsers.templates.HourMinuteSecondParser;

/**
 * English parser that handles 12-hour time in the format HH:MM:SS AM/PM. AM/PM
 * is required. AM/PM can also be written with dots: A.M./P.M. The seconds are
 * optional.
 * 
 * @author hashadex
 * @see io.github.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser
 * @since 1.0.0
 */
public final class ENTwelveHourTimeParser extends HourMinuteSecondParser {
    /**
     * Constructs the parser
     * 
     * @since 1.0.0
     */
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
            (?=$|\\s)                 # Right boundary check
            """
        );
    }
}
