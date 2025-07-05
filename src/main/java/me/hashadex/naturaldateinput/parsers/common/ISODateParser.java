package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public class ISODateParser extends Parser {
    public ISODateParser() {
        super(
            """
            (?<=^|\\s) # Left boundary check
            (?<year>\\d{4})
            (?:
                (?<delimeter>-|\\.|)
                (?<month>\\d{2})
                (?:
                    \\k<delimeter> # Match only if first delimeter is same as second
                    (?<day>\\d{2})
                )?
            )?
            (?<=$|\\s) # Right boundary check
            """
        );
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        int year = Integer.parseInt(match.group("year"));

        int month = 1;
        if (match.group("month") != null) {
            month = Integer.parseInt(match.group("month"));
        }
        if (!(month >= 1 && month <= 12)) {
            return Optional.empty();
        }

        int day = 1;
        if (match.group("day") != null) {
            day = Integer.parseInt(match.group("day"));
        }
        if (!YearMonth.of(year, month).isValidDay(day)) {
            return Optional.empty();
        }

        LocalDate result = LocalDate.of(year, month, day);

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).date(result).build()
        );
    }
}
