package me.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public class HourMinuteParser extends Parser {
    public HourMinuteParser(String regex, int flags) {
        super(regex, flags);
    }

    public HourMinuteParser(String regex) {
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

        int hour = 0;
        if (match.namedGroups().containsKey("hour") && match.group("hour") != null) {
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
