package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public class FormalFormatParser extends Parser {
    public enum DateFormat {
        DAY_MONTH,
        MONTH_DAY
    }

    private final DateFormat preferredDateFormat;

    public FormalFormatParser(DateFormat preferredDateFormat) {
        super(
            """
            (?<=^|\\s)                     # Left boundary check
            (?<block1>\\d{4}|\\d{1,2})     # First block (4 digit year or month/day)
            (?<delimeter>[\\.\\/\\-])      # Delimeter
            (?<block2>\\d{1,2})            # Second block (month/day)
            (?:
                \\k<delimeter>             # Match only if the second delimeter equals the first
                (?<block3>\\d{4}|\\d{1,2}) # Optional third block (4 digit year or month/day)
            )?
            (?=$|\\s)                      # Right boundary check
            """
        );

        this.preferredDateFormat = preferredDateFormat;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(LocalDateTime reference, String source, MatchResult match) {
        int block1 = Integer.parseInt(match.group("block1"));
        int block2 = Integer.parseInt(match.group("block2"));
        
        // Check that all blocks (except year blocks) are in range 1-31
        if (match.group("block3") == null) {
            // DD/MM
            // MM/DD
            if (!isWithinDayRange(block1) || !isWithinDayRange(block2)) {
                return Optional.empty();
            }
        } else {
            int block3 = Integer.parseInt(match.group("block3"));

            if (is4DigitNumber(block1)) {
                // YYYY-MM-DD
                if (!isWithinDayRange(block2) || !isWithinDayRange(block3)) {
                    return Optional.empty();
                }
            } else {
                // DD/MM/YYYY
                // MM/DD/YYYY
                if (!isWithinDayRange(block1) || !isWithinDayRange(block2)) {
                    return Optional.empty();
                } 
            }
        }

        // Check for YYYY-MM-DD
        if (match.group("block3") != null) {
            int block3 = Integer.parseInt(match.group("block3"));

            if (is4DigitNumber(block1) && isWithinMonthRange(block2) && isWithinDayRange(block3)) {
                YearMonth yearMonth = YearMonth.of(block1, block2);

                if (block3 <= yearMonth.lengthOfMonth()) {
                    return Optional.of(
                        new ParsedComponentBuilder(reference, source, match).start(yearMonth.atDay(block3)).build()
                    );
                } else {
                    return Optional.empty();
                }
            }
        }

        // At this point, the date must be of format:
        // DD/MM
        // DD/MM/YYYY
        // MM/DD
        // MM/DD/YYYY

        // Check if at least one block is within month range
        if (!isWithinMonthRange(block1) && !isWithinMonthRange(block2)) {
            return Optional.empty();
        }

        // Find which block is month and which is day
        Month month;
        int day;

        if (!isWithinMonthRange(block1)) {
            // DD/MM
            month = Month.of(block2);
            day = block1;
        } else if (!isWithinMonthRange(block2)) {
            // MM/DD
            month = Month.of(block1);
            day = block2;
        } else {
            // Ambiguous, like 03/05
            if (preferredDateFormat == DateFormat.DAY_MONTH) {
                month = Month.of(block2);
                day = block1;
            } else {
                month = Month.of(block1);
                day = block2;
            }
        }

        // Check if day is valid for current month
        if (day > month.maxLength()) {
            return Optional.empty();
        }

        // Assemble the date
        int year;
        boolean yearSetExplicitly = false;

        if (match.group("block3") != null) {
            year = normalizeYear(Integer.parseInt(match.group("block3")));
            yearSetExplicitly = true;
        } else {
            year = reference.getYear();
        }

        LocalDate result = MonthDay.of(month, day).atYear(year);

        if (result.isBefore(reference.toLocalDate()) && !yearSetExplicitly) {
            result = result.plusYears(1);
        }

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).start(result).build()
        );
    }
}
