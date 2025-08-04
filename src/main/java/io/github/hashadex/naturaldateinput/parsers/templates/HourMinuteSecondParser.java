package io.github.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.regex.MatchResult;

import io.github.hashadex.naturaldateinput.parsers.Parser;

/**
 * Base for parsers that handle time in formats like HH:MM:SS with support for
 * AM/PM.
 * <p>
 * The regex may have <code>hour</code>, <code>minute</code> and
 * <code>second</code> named capturing groups, however none of these groups are
 * required. If any of these groups are missing, then their respective field is
 * always assumed to be 0. These groups must only match digits.
 * <p>
 * Both 24-hour time and 12-hour time is supported. The parser uses 24-hour by
 * default, unless either the <code>am</code> or <code>pm</code> optional
 * capturing group captures anything. If the <code>am</code> group captures
 * anything, then the parser works in AM mode and vice versa, however both of
 * these groups must not be active at once.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public class HourMinuteSecondParser extends Parser {
    /**
     * Constructs the parser using custom regex flags. See the
     * {@link HourMinuteSecondParser class doc comment} for requirements for
     * the regexes and maps.
     * 
     * @param regex Regex for the parser
     * @param flags Bit mask of the regex flags that will be passed
     *              to {@link java.util.regex.Pattern#compile(String, int)}
     */
    public HourMinuteSecondParser(String regex, int flags) {
        super(regex, flags);
    }

    /**
     * Constructs the parser using default regex flags. See the
     * {@link HourMinuteSecondParser class doc comment} for requirements for
     * the regexes and maps.
     * 
     * @param regex Regex for the parser
     */
    public HourMinuteSecondParser(String regex) {
        super(regex);
    }

    private static boolean isWithin24HourRange(int hour) {
        return hour >= 0 && hour <= 23;
    }

    private static boolean isWithin12HourRange(int hour) {
        return hour >= 0 && hour <= 12;
    }

    private static boolean isWithinMinuteSecondRange(int value) {
        return value >= 0 && value <= 59;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        boolean am = false;
        if (match.namedGroups().containsKey("am") && match.group("am") != null) {
            am = true;
        }

        boolean pm = false;
        if (match.namedGroups().containsKey("pm") && match.group("pm") != null) {
            pm = true;
        }
        // If the regex is working correctly, either am or pm can be true.

        int hour = 0;
        if (match.namedGroups().containsKey("hour") && match.group("hour") != null) {
            hour = Integer.parseInt(match.group("hour"));
            
            if (am || pm) {
                if (!isWithin12HourRange(hour)) {
                    return Optional.empty();
                }
            } else {
                if (!isWithin24HourRange(hour)) {
                    return Optional.empty();
                }
            }

            // Convert 12h to 24h, if am or pm is present
            if (am && hour == 12) {
                // 12 AM means 0:00
                hour = 0;
            } else if (pm && hour != 12) {
                // 12 PM means 12:00
                hour += 12;
            }
        }

        int minute = 0;
        if (match.namedGroups().containsKey("minute") && match.group("minute") != null) {
            minute = Integer.parseInt(match.group("minute"));

            if (!isWithinMinuteSecondRange(minute)) {
                return Optional.empty();
            }
        }

        int second = 0;
        if (match.namedGroups().containsKey("second") && match.group("second") != null) {
            second = Integer.parseInt(match.group("second"));

            if (!isWithinMinuteSecondRange(second)) {
                return Optional.empty();
            }
        }

        // Assemble the date
        LocalTime result = LocalTime.of(hour, minute, second);

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).time(result).build()
        );
    }
}
