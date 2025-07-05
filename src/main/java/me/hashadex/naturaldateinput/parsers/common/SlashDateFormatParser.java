package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public class SlashDateFormatParser extends Parser {
    public static enum DayMonthOrder {
        DAY_MONTH,
        MONTH_DAY
    }

    private final DayMonthOrder preferredDayMonthOrder;

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
            """
        );

        this.preferredDayMonthOrder = preferredDayMonthOrder;
    }

    // private static DayMonthOrder resolveOrderFromLocale(Locale locale) {
    //     LocalDate date = LocalDate.of(9999, 12, 31);

    //     DateTimeFormatter localizedFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale);

    //     String formattedDate = date.format(localizedFormatter);

    //     if (formattedDate.startsWith("31")) {
    //         return DayMonthOrder.DAY_MONTH;
    //     } else if (formattedDate.startsWith("12")) {
    //         return DayMonthOrder.MONTH_DAY;
    //     }
    // }

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
        if (match.group("year") != null) {
            String yearString = match.group("year");

            if (yearString.length() == 2) {
                yearString = "20" + yearString;
            }

            year = Integer.parseInt(yearString);
            yearSetExplicitly = true;
        }

        int num1 = Integer.parseInt(match.group("num1"));
        int num2 = Integer.parseInt(match.group("num2"));

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
