package io.github.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import io.github.hashadex.naturaldateinput.parsers.Parser;

/**
 * Parser for the ISO 8601 (YYYY-MM-DD) date format. Supports the dash delimeter
 * (<code>-</code>), dot delimeter (<code>.</code>) and no delimeter (basic
 * format, <code>"20250802"</code>).
 * 
 * @author hashadex
 * @since 1.0.0
 */
public class ISODateParser extends Parser {
    /**
     * Constructs the parser.
     * 
     * @since 1.0.0
     */
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
            """,
            Map.of("year", 1, "delimeter", 2, "month", 3, "day", 4)
        );
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        int year = Integer.parseInt(match.group(namedGroupMap.get("year")));

        int month = 1;
        if (match.group(namedGroupMap.get("month")) != null) {
            month = Integer.parseInt(match.group(namedGroupMap.get("month")));
        }
        if (!(month >= 1 && month <= 12)) {
            return Optional.empty();
        }

        int day = 1;
        if (match.group(namedGroupMap.get("day")) != null) {
            day = Integer.parseInt(match.group(namedGroupMap.get("day")));
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
