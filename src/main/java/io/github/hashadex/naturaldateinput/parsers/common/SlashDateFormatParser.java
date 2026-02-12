package io.github.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import io.github.hashadex.naturaldateinput.parsers.Parser;

/**
 * {@link io.github.hashadex.naturaldateinput.parsers.Parser Parser} for DMY
 * and MDY dates, such as 31.12.2025 and 6/20/2020. This parser supports two
 * delimeters, dot (<code>.</code>) and slash (<code>/</code>) and can parse
 * dates without a year, such as 07/04. In that case, the year is implied from
 * the reference date.
 * <p>
 * Parsing of ambiguous dates like 2025-12-10 is determined by the preferred
 * {@link DayMonthOrder}, set with the
 * {@link #SlashDateFormatParser(DayMonthOrder) constructor}. For example, if
 * the parser was created with the {@link DayMonthOrder#DAY_MONTH DAY_MONTH}
 * order, then "10.12.2025" is parsed as December 10th, 2025. Otherwise, if the
 * parser was created with the {@link DayMonthOrder#MONTH_DAY MONTH_DAY} order,
 * then "10.12.2025" is parsed as October 12th, 2025. The chosen order does not
 * affect the parsing of non-ambiguous dates like "31.12.2025".
 * 
 * @author hashadex
 * @see io.github.hashadex.naturaldateinput.parsers.common.ISODateParser ISODateParser
 * @since 1.0.0
 */
public class SlashDateFormatParser extends Parser {
    /**
     * Order in which day and month is placed.
     * 
     * @see io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser#SlashDateFormatParser(DayMonthOrder)
     * @since 1.0.0
     */
    public static enum DayMonthOrder {
        /**
         * "Little endian" order, used in the DMY format.
         */
        DAY_MONTH,

        /**
         * "Middle endian" order, used in the YMD and MDY formats.
         */
        MONTH_DAY
    }

    private final DayMonthOrder preferredDayMonthOrder;

    /**
     * Constructs the parser and sets the preferred day-month order. See
     * {@link io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser class doc comment}
     * for explanation on what the preferred day-month order affects.
     * 
     * @param preferredDayMonthOrder Preferred day-month order
     * @since 1.0.0
     */
    public SlashDateFormatParser(DayMonthOrder preferredDayMonthOrder) {
        super(
            """
            (?<=^|\\s)                 # Left boundary check
            (?<num1>\\d{1,2})          # First number, can be a day or a month
            (?<delimeter>\\.|/)        # Delimeter, a dot (.) or a slash (/)
            (?<num2>\\d{1,2})          # Second number, can be a day or a month
            (?:                        # Optional year block
                \\k<delimeter>         # Match only if second delimeter is same as first
                (?<year>\\d{4}|\\d{2}) # Year, four digit or two digit
            )?
            (?<=$|\\s)                 # Right boundary check
            """,
            Map.of("num1", 1, "delimeter", 2, "num2", 3, "year", 4)
        );

        this.preferredDayMonthOrder = preferredDayMonthOrder;
    }

    private static boolean isWithinDayRange(int num) {
        return num >= 1 && num <= 31;
    }

    private static boolean isWithinMonthRange(int num) {
        return num >= 1 && num <= 12;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        boolean yearSetExplicitly = false;
        int year = reference.getYear();
        if (match.group(namedGroupMap.get("year")) != null) {
            String yearString = match.group(namedGroupMap.get("year"));

            if (yearString.length() == 2) {
                yearString = "20" + yearString;
            }

            year = Integer.parseInt(yearString);
            yearSetExplicitly = true;
        }

        int num1 = Integer.parseInt(match.group(namedGroupMap.get("num1")));
        int num2 = Integer.parseInt(match.group(namedGroupMap.get("num2")));

        int day;
        int month;

        if (!isWithinDayRange(num1) || !isWithinDayRange(num2)) {
            return Optional.empty();
        }

        if (isWithinMonthRange(num1) && isWithinMonthRange(num2)) {
            // Ambiguous date - use preferred day-month order
            if (preferredDayMonthOrder == DayMonthOrder.DAY_MONTH) {
                day = num1;
                month = num2;
            } else {
                day = num2;
                month = num1;
            }
        } else if (!isWithinMonthRange(num1) && isWithinMonthRange(num2)) {
            // Day month
            day = num1;
            month = num2;
        } else if (isWithinMonthRange(num1) && !isWithinMonthRange(num2)) {
            // Month day
            day = num2;
            month = num1;
        } else {
            return Optional.empty();
        }

        if (!YearMonth.of(year, month).isValidDay(day)) {
            return Optional.empty();
        }

        LocalDate result = LocalDate.of(year, month, day);
        if (!yearSetExplicitly && result.isBefore(reference.toLocalDate())) {
            result = result.plusYears(1);
        }

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).date(result).build()
        );
    }
}
