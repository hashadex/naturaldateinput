package me.hashadex.naturaldateinput.parsers.common;

import me.hashadex.naturaldateinput.parsers.templates.HourMinuteSecondParser;

/**
 * {@link me.hashadex.naturaldateinput.parsers.Parser Parser} for parsing
 * twenty-four hour time, with or without seconds, like <code>"22:20:48"</code>.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class TwentyFourHourTimeParser extends HourMinuteSecondParser {
    /**
     * Constructs the parser.
     * 
     * @since 1.0.0
     */
    public TwentyFourHourTimeParser() {
        super(
            """
            (?<=^|\\s) # Left boundary check
            (?<hour>\\d{1,2})
            :
            (?<minute>\\d{2})
            (?: # Optional seconds component
                :(?<second>\\d{2})
            )?
            (?=$|\\s) # Right boundary check
            """
        );
    }   
}
